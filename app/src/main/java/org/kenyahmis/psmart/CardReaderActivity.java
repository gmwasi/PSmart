package org.kenyahmis.psmart;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.acs.bluetooth.BluetoothReader;

import org.kenyahmis.psmartlibrary.PSmartCard;
import org.kenyahmis.psmartlibrary.Models.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardReaderActivity extends AppCompatActivity {
    ArrayList<String> errors = new ArrayList<>();
    private BluetoothDevice bluetoothDevice;
    ProgressDialog progressDialog;
    BluetoothReaderInitializer initializer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_reader);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);

        String token = getIntent().getExtras().getString(AppConstants.EXTRA_AUTH_TOKEN, null);
        if (token != null && new Tokenizer(getBaseContext()).compareHash(token)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
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

                startActivityForResult(intent,80);
            }


        }else{
            errors.add(getString(R.string.error_auth_failed));
            Intent intent = new Intent();
            intent.putStringArrayListExtra(AppConstants.EXTRA_ERRORS,errors);
            setResult(RESULT_CANCELED,intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==1){
            Intent intent = new Intent(getBaseContext(),DeviceScanActivity.class);
            startActivityForResult(intent,80);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getBaseContext(),"Device scanned", Toast.LENGTH_LONG).show();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==80){
            if(resultCode ==RESULT_OK){
                progressDialog.setCancelable(false);

                bluetoothDevice = data.getParcelableExtra("device");
                if(getIntent().getAction().equals("org.kenyahmis.psmart.ACTION_READ_DATA")){
                    progressDialog.setTitle("Reading Data");
                    progressDialog.show();
                    new ReadTask().execute();
                }else{
                    progressDialog.setTitle("Writing Data");
                    progressDialog.show();
                    String message = getIntent().getExtras().getString("WRITE_DATA", null);
                    if(message!=null)
                        new WriteTask().execute(message);
                }
            }else{
                Intent intent = new Intent();
                errors.add("Could not Connect to card");
                intent.putStringArrayListExtra(AppConstants.EXTRA_ERRORS,errors);
                setResult(RESULT_CANCELED, intent);
            }
        }
    }
    private  void hideDialog(){
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    class ReadTask extends AsyncTask<Response,Void,Response> {

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            hideDialog();
            if(response!=null){
                Intent intent = new Intent();
                intent.putExtra(AppConstants.EXTRA_MESSAGE,response.getMessage());
                setResult(RESULT_OK, intent);
                Toast.makeText(getApplicationContext(), SHR, Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Intent intent = new Intent();
                errors.add("Could not read");
                intent.putStringArrayListExtra(AppConstants.EXTRA_ERRORS,errors);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }

        @Override
        protected Response doInBackground(Response... responses) {
            return read();
        }
    }

    class WriteTask extends AsyncTask<String,Void,Response>{
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            hideDialog();
            if(response!=null){
                Intent intent = new Intent();
                intent.putExtra(AppConstants.EXTRA_MESSAGE,response.getMessage());
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Intent intent = new Intent();
                errors.add("Could not write to card");
                intent.putStringArrayListExtra(AppConstants.EXTRA_ERRORS,errors);
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        }

        @Override
        protected Response doInBackground(String... strings) {
            return write(strings[0]);
        }
    }

    private BluetoothReader connect(){

        if(bluetoothDevice == null){return null;}
        initializer = new BluetoothReaderInitializer(this, bluetoothDevice.getAddress());
        BluetoothReader reader = null;
        try {
            reader = initializer.getReader();
            if(reader == null){
                // display error
            }
        }

        catch(Exception ex){

        }

        return reader;
    }

    private void disconnect(){
        if(initializer != null){
            initializer.disconnectReader();
        }
    }

    private Response read(){
        BluetoothReader bluetoothReader = connect();
        if(bluetoothReader != null){

            PSmartCard pSmartCard = new PSmartCard(bluetoothReader, this.getApplicationContext());
            try {
                Response response = pSmartCard.Read();
                Log.d("Response" , response.getMessage());
                disconnect();
                return response;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        return null;
    }

    private Response write(String shrmessage){
        BluetoothReader bluetoothReader = connect();
        if(bluetoothReader != null){
            try {
                PSmartCard pSmartCard = new PSmartCard(bluetoothReader, this.getApplicationContext());
                Response response = pSmartCard.Write(shrmessage);
                disconnect();
                return response;
            }catch (Exception e){
                return  null;
            }

        }
        return null;
    }

    private void errorMessage(String ... messages){
        Intent intent = new Intent();
        errors.addAll(Arrays.asList(messages));
        intent.putStringArrayListExtra(AppConstants.EXTRA_ERRORS,errors);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

private String SHR  = "{\n" +
            "  \"PATIENT_IDENTIFICATION\": {\n" +
            "    \"EXTERNAL_PATIENT_ID\": {\n" +
            "      \"ID\": \"110ec58a-a0f2-4ac4-8393-c866d813b8d1\",\n" +
            "      \"IDENTIFIER_TYPE\": \"GODS_NUMBER\",\n" +
            "      \"ASSIGNING_AUTHORITY\": \"MPI\",\n" +
            "      \"ASSIGNING_FACILITY\": \"10829\"\n" +
            "    },\n" +
            "    \"INTERNAL_PATIENT_ID\": [\n" +
            "      {\n" +
            "        \"ID\": \"12345678-ADFGHJY-0987654-NHYI890\",\n" +
            "        \"IDENTIFIER_TYPE\": \"CARD_SERIAL_NUMBER\",\n" +
            "        \"ASSIGNING_AUTHORITY\": \"CARD_REGISTRY\",\n" +
            "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"ID\": \"12345678\",\n" +
            "        \"IDENTIFIER_TYPE\": \"HEI_NUMBER\",\n" +
            "        \"ASSIGNING_AUTHORITY\": \"MCH\",\n" +
            "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"ID\": \"12345678\",\n" +
            "        \"IDENTIFIER_TYPE\": \"CCC_NUMBER\",\n" +
            "        \"ASSIGNING_AUTHORITY\": \"CCC\",\n" +
            "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"ID\": \"001\",\n" +
            "        \"IDENTIFIER_TYPE\": \"HTS_NUMBER\",\n" +
            "        \"ASSIGNING_AUTHORITY\": \"HTS\",\n" +
            "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"ID\": \"12345678\",\n" +
            "        \"IDENTIFIER_TYPE\": \"PMTCT_NUMBER\",\n" +
            "        \"ASSIGNING_AUTHORITY\": \"PMTCT\",\n" +
            "        \"ASSIGNING_FACILITY\": \"10829\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"PATIENT_NAME\": {\n" +
            "      \"FIRST_NAME\": \"THERESA\",\n" +
            "      \"MIDDLE_NAME\": \"MAY\",\n" +
            "      \"LAST_NAME\": \"WAIRIMU\"\n" +
            "    },\n" +
            "    \"DATE_OF_BIRTH\": \"20171111\",\n" +
            "    \"DATE_OF_BIRTH_PRECISION\": \"ESTIMATED/EXACT\",\n" +
            "    \"SEX\": \"F\",\n" +
            "    \"DEATH_DATE\": \"\",\n" +
            "    \"DEATH_INDICATOR\": \"N\",\n" +
            "    \"PATIENT_ADDRESS\": {\n" +
            "      \"PHYSICAL_ADDRESS\": {\n" +
            "        \"VILLAGE\": \"KWAKIMANI\",\n" +
            "        \"WARD\": \"KIMANINI\",\n" +
            "        \"SUB_COUNTY\": \"KIAMBU EAST\",\n" +
            "        \"COUNTY\": \"KIAMBU\",\n" +
            "        \"NEAREST_LANDMARK\": \"KIAMBU EAST\"\n" +
            "      },\n" +
            "      \"POSTAL_ADDRESS\": \"789 KIAMBU\"\n" +
            "    },\n" +
            "    \"PHONE_NUMBER\": \"254720278654\",\n" +
            "    \"MARITAL_STATUS\": \"\",\n" +
            "    \"MOTHER_DETAILS\": {\n" +
            "      \"MOTHER_NAME\": {\n" +
            "        \"FIRST_NAME\": \"WAMUYU\",\n" +
            "        \"MIDDLE_NAME\": \"MARY\",\n" +
            "        \"LAST_NAME\": \"WAITHERA\"\n" +
            "      },\n" +
            "      \"MOTHER_IDENTIFIER\": [\n" +
            "        {\n" +
            "          \"ID\": \"1234567\",\n" +
            "          \"IDENTIFIER_TYPE\": \"NATIONAL_ID\",\n" +
            "          \"ASSIGNING_AUTHORITY\": \"GOK\",\n" +
            "          \"ASSIGNING_FACILITY\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"ID\": \"12345678\",\n" +
            "          \"IDENTIFIER_TYPE\": \"NHIF\",\n" +
            "          \"ASSIGNING_AUTHORITY\": \"NHIF\",\n" +
            "          \"ASSIGNING_FACILITY\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"ID\": \"12345-67890\",\n" +
            "          \"IDENTIFIER_TYPE\": \"CCC_NUMBER\",\n" +
            "          \"ASSIGNING_AUTHORITY\": \"CCC\",\n" +
            "          \"ASSIGNING_FACILITY\": \"10829\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"ID\": \"ABC567\",\n" +
            "          \"IDENTIFIER_TYPE\": \"ANC_NUMBER\",\n" +
            "          \"ASSIGNING_AUTHORITY\": \"ANC\",\n" +
            "          \"ASSIGNING_FACILITY\": \"10829\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  },\n" +
            "  \"NEXT_OF_KIN\": [\n" +
            "    {\n" +
            "      \"NOK_NAME\": {\n" +
            "        \"FIRST_NAME\": \"WAIGURU\",\n" +
            "        \"MIDDLE_NAME\": \"KIMUTAI\",\n" +
            "        \"LAST_NAME\": \"WANJOKI\"\n" +
            "      },\n" +
            "      \"RELATIONSHIP\": \"**AS DEFINED IN GREENCARD\",\n" +
            "      \"ADDRESS\": \"4678 KIAMBU\",\n" +
            "      \"PHONE_NUMBER\": \"25489767899\",\n" +
            "      \"SEX\": \"F\",\n" +
            "      \"DATE_OF_BIRTH\": \"19871022\",\n" +
            "      \"CONTACT_ROLE\": \"T\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"HIV_TESTS\": [\n" +
            "    {\n" +
            "      \"DATE\": \"20180101\",\n" +
            "      \"RESULT\": \"POSITIVE/NEGATIVE/INCONCLUSIVE\",\n" +
            "      \"TYPE\": \"SCREENING/CONFIRMATORY\",\n" +
            "      \"FACILITY\": \"10829\",\n" +
            "      \"STRATEGY\": \"HP/NP/VI/VS/HB/MO/O\",\n" +
            "      \"PROVIDER_DETAILS\": {\n" +
            "        \"NAME\": \"AFYA JIJINI***STILL IN REVIEW\"\n" +
            "      }\n" +
            "    }\n" +
            "  ],\n" +
            "  \"IMMUNIZATIONS\": [\n" +
            "    {\n" +
            "      \"NAME\": \"BCG/OPV_AT_BIRTH/OPV1/OPV2/OPV3/PCV10-1/PCV10-2/PCV10-3/PENTA1/PENTA2/PENTA3/MEASLES6/MEASLES9/MEASLES18/ROTA1/ROTA2\",\n" +
            "      \"DATE_ADMINISTERED\": \"20180101\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"MERGE_PATIENT_INFORMATION\": {\n" +
            "    \"PRIOR_INTERNAL_IDENTIFIERS\": [\n" +
            "      {\n" +
            "        \"ID\": \"12345678-67676767-0987654-XXXXYYYY\",\n" +
            "        \"IDENTIFIER_TYPE\": \"CARD_SERIAL_NUMBER\",\n" +
            "        \"ASSIGNING_AUTHORITY\": \"CARD_REGISTRY\",\n" +
            "        \"ASSIGNING_FACILITY\": \"12345\",\n" +
            "        \"REPLACEMENT_REASON\": \"LOST\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"CARD_DETAILS\": {\n" +
            "    \"STATUS\": \"ACTIVE/INACTIVE\",\n" +
            "    \"REASON\": \"\",\n" +
            "    \"LAST_UPDATED\": \"20180101\",\n" +
            "    \"LAST_UPDATED_FACILITY\": \"10829\"\n" +
            "  }\n" +
            "}";
}
