package org.kenyahmis.psmartlibrary.DAL;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Muhoro on 3/20/2018.
 */

public class PSmartFile {

    String filename;
    Context context;

    public PSmartFile(Context context, @NonNull String pathname) {
        this.filename = pathname;
        this.context = context;
    }

    public void write(String dataToWrite) throws Exception{
        FileOutputStream fileOutputStream;
        fileOutputStream = context.openFileOutput(this.filename, Context.MODE_PRIVATE);
        fileOutputStream.write(dataToWrite.getBytes());
        fileOutputStream.close();
    }

    public String read() throws Exception{
        FileInputStream fileInputStream;
        fileInputStream = context.openFileInput(this.filename);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String val;
        while((val = bufferedReader.readLine()) != null){
            stringBuilder.append(val);
        }
        return stringBuilder.toString();
    }
}
