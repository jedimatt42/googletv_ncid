package net.cwfk.ig88.ncid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplayActivity extends Activity {

    TextView cidName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.cid);
        cidName = (TextView) findViewById(R.id.cid_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( cidName != null ) {
            Intent intent = getIntent();
            String label = intent.getStringExtra( "LABEL" );
            String number = intent.getStringExtra( "NUMBER" );

            String msg = String.format( "%s - %s", label, convertPhoneNumber(number) );
            cidName.setText( msg );
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 9000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    Pattern phone = Pattern.compile("\\(?(\\d{3})?\\)?[- \\.]?(\\d{3})?[- \\.]?(\\d{4})");

    private String convertPhoneNumber( String input ) {
        Matcher matcher = phone.matcher( input );
        if ( ! matcher.matches() ) {
            return input;
        }

        if ( matcher.group(2) == null ) {
            return matcher.group(1) + "-" + matcher.group(3);
        } else {
            return "(" + matcher.group(1) + ")" + matcher.group(2) + "-" + matcher.group(3);
        }

    }

}
