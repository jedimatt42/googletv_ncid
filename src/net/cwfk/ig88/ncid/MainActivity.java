package net.cwfk.ig88.ncid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    Config config;

    EditText viewAddress;

    EditText viewPort;

    Button viewSave;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);

        this.config = new Config( this );

        viewAddress = (EditText) findViewById(R.id.address);
        viewPort = (EditText) findViewById(R.id.port);
        viewSave = (Button) findViewById(R.id.save_button);

        viewAddress.setText( config.getAddress() );
        viewPort.setText( "" + config.getPort() );

        viewSave.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                config.setAddress( viewAddress.getText().toString() );
                config.setPort( Integer.parseInt( viewPort.getText().toString() ));
                restartService();
                finish();
            }
        });

        restartService();
    }

    private void restartService() {
        startService(new Intent("net.cwfk.ig88.ncid.ACTION_RESTART"));
    }
}