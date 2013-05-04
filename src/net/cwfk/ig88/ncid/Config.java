package net.cwfk.ig88.ncid;

import android.content.Context;
import android.content.SharedPreferences;

public class Config {

    private static final String PrefsName = "prefs_ncid";
    private static final String Address = "pref_address";
    private static final String Port = "pref_port";

    private final Context context;

    public Config( Context context ) {
        this.context = context;
    }

    public String getAddress() {
        return prefs().getString( Address, "ncid.local" );
    }

    public void setAddress( String address ) {
        prefs().edit().putString( Address, address ).commit();
    }

    public Integer getPort() {
        return prefs().getInt( Port, 3333 );
    }

    public void setPort( Integer port ) {
        prefs().edit().putInt( Port, port ).commit();
    }

    private SharedPreferences prefs() {
        return context.getSharedPreferences(PrefsName, Context.MODE_PRIVATE);
    }
}
