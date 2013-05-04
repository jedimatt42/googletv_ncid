package net.cwfk.ig88.ncid;

import android.util.Log;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NCIDClient {

    CIDHandler cidHandler;
    StatusHandler statusHandler;
    BufferedReader isr;
    Writer osw;
    boolean stopRequested = false;
    private Config config;

    public NCIDClient( Config config ) {
        this.config = config;
    }

    public void setCIDHandler( CIDHandler handler ) {
        this.cidHandler = handler;
    }

    public void setStatusHandler( StatusHandler statusHandler ) {
        this.statusHandler = statusHandler;
    }

    void watch() {
        Socket client = null;
        while( ! stopRequested ) {
            while(( ! stopRequested ) && ( client == null || ! client.isConnected() )) {
                try {
                    String host = config.getAddress();
                    int port = config.getPort();
                    client = new Socket( host, port );
                    isr = new BufferedReader( new InputStreamReader( client.getInputStream() ) );
                    osw = new OutputStreamWriter( client.getOutputStream() );
                    Log.d("IG88", "connected to " + host + " " + port );
                    statusHandler.onStateChange( "Connected" );
                    createWatchdog();
                } catch (IOException e) {
                    Log.d( "IG88", "failed to connect: " + e.getMessage() );
                    attemptSleep( 60000L );
                    osw = null;
                    isr = null;
                    client = null;
                }
            }

            String line = null;
            try {
                while( (! stopRequested) && ( (line = isr.readLine()) != null )) {
                    Log.d( "IG88", "NCID said: " + line );
                    if ( stopRequested ) {
                        break;
                    }
                    handleLine( line );
                }
            } catch (IOException e) {
                // we've become disconnected some how... so loop back up.
            }

            if ( client != null && !client.isClosed() ) {
                try {
                    client.close();
                    isr = null;
                    osw = null;
                    client = null;
                } catch (IOException e) {
                    // Ignore... we tried to close...
                }
                statusHandler.onStateChange( "Disconnected" );
            }
        }
    }

    private void createWatchdog() {
        Thread watchdog = new Thread( new Runnable() {
            public void run() {
                Log.d("IG88", "NCID connection watchdog started");
                while( ! stopRequested ) {
                    try { Thread.sleep( 30000 ); } catch ( Exception e ) { /* ignore */ }
                    try {
                        if ( osw != null ) { osw.write("\n"); }
                    } catch (IOException e) {
                        Log.d( "IG88", "watchdog detected disconnect" );
                        return;
                    }
                }
            }
        }, "NCID Watchdog" );
        watchdog.start();
    }

    public void stop() {
        stopRequested = true;
    }

    private void attemptSleep( long millis ) {
        try {
            Thread.sleep( millis );
        } catch (InterruptedException e1) {
            // ignore.
        }
    }


    /* Lines may look like:
        200 Server: ncidd (NCID) 0.85
        CIDLOG: *DATE*10282012*TIME*1439*LINE*-*NMBR*5033803730*MESG*NONE*NAME*SPLETT,MATTHEW*
        300 end of call log
        CID: *DATE*10282012*TIME*1442*LINE*-*NMBR*5033803730*MESG*NONE*NAME*SPLETT,MATTHEW*
     */

    void handleLine( String line ) {
        if ( line.startsWith( "CID: ")) {
            String name = "unknown";
            String number = "###";
            String[] parts = line.split( "\\*" );
            for( int i = 1; i < parts.length; i+=2 ) {
                String key = parts[i];
                String value = parts[i+1];

                if ( key.equalsIgnoreCase("NAME")) {
                    name = value;
                }
                else if ( key.equalsIgnoreCase( "NMBR" ) ) {
                    number = value;
                }
            }
            handleCID(name, number);
        }
    }

    void handleCID( String label, String number ) {
        if ( cidHandler != null ) {
            cidHandler.onCID( label, number );
        }
    }
}
