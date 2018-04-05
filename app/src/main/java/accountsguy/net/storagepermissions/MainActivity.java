package accountsguy.net.storagepermissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

//    File internalFile;
    private EditText dataEditText, cacheEditText;

    private TextView cacheTextView, textView;

    private File directory, file, cacheFile;
    private FileWriter fileWriter;

    private BufferedReader bufferedReader  = null;
    private FileInputStream fileInputStream = null;

    private String state;
    private int writePermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button writeinternalstorageButton = (Button) findViewById(R.id.writeinteralstorage_button);
        Button writeexternalstorageButton = (Button) findViewById(R.id.writeexteralstorage_button);
        Button readinternalstorageButton = (Button) findViewById(R.id.readinteralstorage_button);
        Button readexternalstorageButton = (Button) findViewById(R.id.readexteralstorage_button);
        Button contactpermissionButton = (Button) findViewById(R.id.contacts_permission);
        Button internetpermissionButton = (Button) findViewById(R.id.internet_permission);
        Button writecacheButton = (Button) findViewById(R.id.writecache_button);
//        Button readcacheButton = (Button) findViewById(R.id.readcache_button);

        textView = (TextView) findViewById(R.id.showdata);

        writeinternalstorageButton.setOnClickListener(this);
        writeexternalstorageButton.setOnClickListener(this);
        readexternalstorageButton.setOnClickListener(this);
        readinternalstorageButton.setOnClickListener(this);
        contactpermissionButton.setOnClickListener(this);
        internetpermissionButton.setOnClickListener(this);
        writecacheButton.setOnClickListener(this);
//        readcacheButton.setOnClickListener(this);

        dataEditText = (EditText) findViewById(R.id.data);
        cacheEditText = (EditText) findViewById(R.id.data);

        cacheTextView = (TextView) findViewById(R.id.showcachedata);

        directory = new File(getFilesDir(), "");
        file = new File(directory, "InternalFile.txt");
        state = Environment.getExternalStorageState();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 101:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Contacts Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                }
                else {
                    Toast.makeText(this, "Contacts Permission Denaied", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            case 102:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Write External File Permission Granted", Toast
                            .LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Write External File Permission Denaied", Toast.LENGTH_SHORT).show();
                }
                return;
            case 103:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Read External File Permission Granted", Toast
                            .LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Read External File Permission Denaied", Toast.LENGTH_SHORT).show();
                }
                return;

            case 104:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Internet ermission Granted", Toast
                            .LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Internet Permission Denaied", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.writeinteralstorage_button:
                try{
                    fileWriter = new FileWriter(file);

                    if(!directory.exists()){
                        directory.mkdir();
                        fileWriter.append("Internal Storage is non-volatile storage device which " +
                                "is your device memory./n");
                        fileWriter.close();
                        Toast.makeText(this, "Internal Storage Directory Created",Toast
                                .LENGTH_SHORT).show();
                    }
                    else{
                        fileWriter.append(dataEditText.getText().toString().trim());
                        Toast.makeText(this, "Data Recorded in Interal Storage File",
                                Toast
                                .LENGTH_SHORT)
                                .show();
                    }
                    fileWriter.flush();
                    fileWriter.close();
                }
                catch (Exception e){
                    Toast.makeText(this, "Exception:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                return;
            case R.id.writeexteralstorage_button:
                writePermission = checkSelfPermission(Manifest.permission
                        .WRITE_EXTERNAL_STORAGE);

                if(writePermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            102);
                }
                try {
                    File directory = Environment.getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOCUMENTS);
                    //                    File privateDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        file = new File(directory, "ExternalFile.txt");
                        fileWriter = new FileWriter(file);
                        fileWriter.append(dataEditText.getText().toString().trim());
                        fileWriter.flush();
                        fileWriter.close();
                        Toast.makeText(this, "Data Recorded on Enteral Storage File " ,Toast
                                .LENGTH_SHORT).show();
                    } else {
                        return;
                    }
                }
                catch (Exception e){
                    Toast.makeText(this, "Exception: "+e.getMessage(),Toast.LENGTH_SHORT)
                            .show();
                }
                return;

            case R.id.readinteralstorage_button:

                try {
                    directory = new File(getFilesDir(), "");
                    file = new File(directory, "InternalFile.txt");
                    fileInputStream = new FileInputStream(file);
                    bufferedReader = new BufferedReader(new InputStreamReader
                            (fileInputStream));

                    if(!directory.exists()){
                        Toast.makeText(this, "No Internal Storage Directory found",Toast
                                .LENGTH_SHORT).show();
                    }
                    else{
                        String line = null;
                        String values = "";
                        while((line = bufferedReader.readLine()) != null )
                        {
                            values = values.concat(line);
                        }

                        textView.setText(values);
                        textView.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                } finally {
                    if(bufferedReader!=null)
                        try{
                            bufferedReader.close();
                        } catch (Exception e) {
                            Toast.makeText(this, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                }
                return;
            case R.id.readexteralstorage_button:
                int readPermission = checkSelfPermission(Manifest.permission
                        .READ_EXTERNAL_STORAGE);

                if(readPermission != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            103);
                } else {
                    state = Environment.getExternalStorageState();
                    try {
                        directory = Environment.getExternalStoragePublicDirectory
                                (Environment
                                        .DIRECTORY_DOCUMENTS);
                        //                    File privateDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            file = new File(directory, "ExternalFile.txt");
                            Scanner input = new Scanner(file);

                            StringBuilder values=new StringBuilder();
                            while(input.hasNextLine()){
                                values.append(input.nextLine());
                            }

                            textView.setText(values);
                            textView.setVisibility(View.VISIBLE);
                        } else {
                            return;
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                return;

            case R.id.writecache_button:
                try{
                    String filename = "CacheFile";
                    cacheFile = File.createTempFile(filename, ".txt", getCacheDir());
                    fileWriter = new FileWriter(cacheFile);
                    fileWriter.append(cacheEditText.getText().toString().trim());
                    fileWriter.flush();
                    fileWriter.close();

                    Toast.makeText(this, "Data Recorded in Cache File",Toast
                            .LENGTH_LONG).show();
                 }
                catch (Exception e){
                    Toast.makeText(this, "Exception: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
                return;
//            case R.id.readcache_button:
//                try {
////                    fileInputStream = new FileInputStream(cacheFile);
////                    Log.i("Test - ", cacheFile.getCanonicalPath());
//
//                    FileReader fileReader = new FileReader(cacheFile);
//                    bufferedReader = new BufferedReader(fileReader);
//
//                    String  line = null;
//                    StringBuilder  stringBuilder = new StringBuilder();
//                    Log.i("Test - ", stringBuilder.toString());
////                    String ls = System.getProperty("line.separator");
//
//                        while((line = bufferedReader.readLine()) != null) {
//                            stringBuilder.append(line);
////                            stringBuilder.append(ls);
//                        }
//
////                    char[] data = new char[5];
////                    fileReader.read(data);
//                    Log.i("Test - ", stringBuilder.toString());
////                    int data= fileReader.read();
////                    while(data != -1){
////                        data = (char)data;
////                        stringBuffer.append(data);
////                        Log.i("Test - ", "While");
////                    }
////
////                    Log.i("Test :", stringBuffer.toString());
//
//                    cacheTextView.setText(stringBuilder.toString());
//                    cacheTextView.setVisibility(View.VISIBLE);
//                } catch (Exception e) {
//                    if(cacheFile!=null) {
//                        Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                    else Toast.makeText(this, "No Cache File Created", Toast.LENGTH_LONG).show();
//                } finally {
//                    if(bufferedReader!=null)
//                        try{
//                            bufferedReader.close();
//                        } catch (Exception e) {
//                            Toast.makeText(this, "Exception 2: "+e.getMessage(), Toast
//                                    .LENGTH_SHORT).show();
//                        }
//                }
//                return;

            case R.id.contacts_permission:
                int contactPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);

                // Reqeustcode  101 is user defined we can define our own.
                if(contactPermission != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 101);
                }
                return;

            case R.id.internet_permission:
                int internetPermission = checkSelfPermission(Manifest.permission.INTERNET);
                if(internetPermission != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.INTERNET}, 104);
                }
                else{
                    Toast.makeText(this, "Internet Permission is Normal Permission by default " +
                            "system will allow to access internet", Toast.LENGTH_LONG).show();
                }
        }
    };
}
