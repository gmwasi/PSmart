package org.kenyahmis.psmart;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class CardReaderActivity extends AppCompatActivity {
    ArrayList<String> errors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reader);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String token = getIntent().getExtras().getString(AppConstants.EXTRA_AUTH_TOKEN, null);
        if (token != null && new Tokenizer(getBaseContext()).compareHash(token)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(this, "Please give us your location permissions to continue", Toast.LENGTH_SHORT).show();

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            1);
                }

            } else {
                Intent intent = new Intent(getBaseContext(),DeviceScanActivity.class);
                startActivityForResult(intent,1);
            }


        }else{
            setResult(RESULT_CANCELED);
            errors.add(getString(R.string.error_auth_failed));
            Intent intent = new Intent();
            intent.putStringArrayListExtra(AppConstants.EXTRA_ERRORS,errors);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
private void getPermisions(){

}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getBaseContext(),"Device scanned", Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void readJob()  {
        if(true){
            Intent intent = new Intent();
            intent.putExtra(AppConstants.EXTRA_MESSAGE,"JSON String");
            setResult(RESULT_OK, intent);

        }else{
            Intent intent = new Intent();
            errors.add("Could not write");
            intent.putStringArrayListExtra(AppConstants.EXTRA_ERRORS,errors);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

}
