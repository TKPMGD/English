package com.pmt.cis.english;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    RelativeLayout myLayout;
    AnimationDrawable animationDrawable;
    Button btnStart;
    CheckBox ckbRemember;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 6384;
    private static final int READ_REQUEST_CODE = 42;
    String dataPath;
    String arrayName[] = {"Facebook", "Drive", "Google"};
    String myData;
    private String file = "mydata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout = (RelativeLayout) findViewById(R.id.myLayout);
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);

        }


        btnStart = (Button) findViewById(R.id.btnStart);
        dataPath = getData(file);
        Toast.makeText(getApplicationContext(),
                dataPath,
                Toast.LENGTH_LONG).show();
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chooserFile();
                Intent intent = new Intent(MainActivity.this,Reading.class);
                startActivity(intent);
            }
        });
        ckbRemember = (CheckBox) findViewById(R.id.ckbRemember);
        if (dataPath != null && !dataPath.equals("")) {
            ckbRemember.setChecked(true);
        }

        ckbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (dataPath != null && !isChecked) {
                    showAlertDialog();
                }
            }
        });


    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(MainActivity.this);

        alertDialogBuilder.setTitle("Clear data file location");
        alertDialogBuilder.setMessage("Are you sure?");
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearPath();

                    }
                });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancel the alert box and put a Toast to the user
                        dialog.cancel();
                        Toast.makeText(getApplicationContext(),
                                "You chose a negative answer",
                                Toast.LENGTH_LONG).show();
                    }
                });

        AlertDialog alertDialog =
                alertDialogBuilder.create();
        alertDialog.show();
    }

    private String getData(String fileName) {

        try {
            FileInputStream fin = openFileInput(fileName);
            int c;
            String temp = "";
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);

            }
            return temp;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void savePath() {

        try {
            FileOutputStream fout = openFileOutput(file, MODE_PRIVATE);
            fout.write(dataPath.getBytes());
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void clearPath() {
        File dir = getFilesDir();
        File myfile = new File(dir, file);
        myfile.delete();
    }

    private void chooserFile() {
        new MaterialFilePicker()
                .withActivity(this)
                //.withFilter(Pattern.compile(".*\\.txt$"))
                .withRequestCode(1000)
                .start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            if (ckbRemember.isChecked()) {
                dataPath = filePath;
                savePath();
            }
            StringBuilder text=getdata(filePath);
            Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();
            startLearning();

        }
    }


    private StringBuilder getdata(String path) {
        BufferedReader br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(path));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            String line = "";
            StringBuilder tmp= new StringBuilder();
            while ((line = br.readLine()) != null) {
                tmp.append(line);
                tmp.append("\n");
            }
            return tmp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startLearning(){
        Intent intent = new Intent(this,studyingActivity.class);
        startActivity(intent);

    }
}
