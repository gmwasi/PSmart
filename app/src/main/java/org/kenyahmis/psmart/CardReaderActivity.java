package org.kenyahmis.psmart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
            readJob();

        }else{
            setResult(RESULT_CANCELED);
            errors.add(getString(R.string.error_auth_failed));
            Intent intent = new Intent();
            intent.putStringArrayListExtra(AppConstants.EXTRA_ERRORS,errors);
            setResult(RESULT_OK, intent);
            finish();
        }
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
