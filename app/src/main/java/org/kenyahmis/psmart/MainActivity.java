package org.kenyahmis.psmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getBaseContext(),CardReaderActivity.class);
        //intent.putExtra("ACTION", "WRITE");
        intent.setAction("org.kenyahmis.psmart.ACTION_READ_DATA");
        //intent.putExtra("WRITE_DATA", "SHM_DATA_HERE");
        intent.putExtra(AppConstants.EXTRA_AUTH_TOKEN,"123");
        startActivity(intent);
    }
}
