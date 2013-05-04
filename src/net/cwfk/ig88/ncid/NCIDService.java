package net.cwfk.ig88.ncid;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NCIDService extends Service {

    NCIDClient client = null;

    CIDHandler handler = new CIDHandler() {
        public void onCID(String label, String number) {
            String msg = String.format("Call from: %s - %s", label, number);
            Log.d( "IG88", "CIDHandler msg: " + msg );
            Intent cidIntent = new Intent( "net.cwfk.ig88.ncid.ACTION_SHOW_CID");
            cidIntent.putExtra( "LABEL", label );
            cidIntent.putExtra( "NUMBER", number );
            cidIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
            startActivity( cidIntent );
        }
    };

    StatusHandler statusHandler = new StatusHandler() {
        public void onStateChange(String status) {
            notification(status);
        }
    };

    Thread t;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if ( intent != null && intent.getAction().endsWith( "ACTION_RESTART" ) ) {
            Log.d("IG88", "NCIDService starting..." );
            notification("Pending");

            startNCIDClient();
        } else if ( intent != null && intent.getAction().endsWith( "NCID_CONNECTED" ) ) {
            notification("Connected");
        } else if ( intent != null && intent.getAction().endsWith( "NCID_DISCONNECTED" ) ) {
            notification("Disconnected");
        }

        return START_STICKY;
    }

    private void notification(String status) {
        Context appContext = getApplicationContext();

        Notification notification = new Notification.Builder(appContext)
                .setDefaults( Notification.DEFAULT_ALL )
                .setContentTitle("NCID " + status)
                .setSmallIcon( R.drawable.ic_launcher )
                .setOngoing(true)
                .setAutoCancel( true )
                .getNotification();

        NotificationManager nm = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(2525);
        startForeground(2525, notification);
    }

    private void startNCIDClient() {
        if ( client != null ) {
            client.stop();
            client = null;
        }
        t = new Thread( new Runnable() {
            public void run() {
                client = new NCIDClient( new Config( getApplicationContext() ) );
                client.setCIDHandler( handler );
                client.setStatusHandler( statusHandler );
                client.watch();
            }
        });
        t.start();
        Log.d( "IG88", "started NCID client thread" );
        Toast.makeText( this, "NCID Monitor enabled", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onDestroy() {
        if ( client != null ) {
            client.stop();
            client = null;
        }
        super.onDestroy();
    }
}
