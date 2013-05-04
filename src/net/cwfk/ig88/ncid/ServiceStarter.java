package net.cwfk.ig88.ncid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceStarter extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.d("IG88", "boot completed received" );
        context.startService( new Intent( "net.cwfk.ig88.ncid.ACTION_RESTART" ));
    }
}
