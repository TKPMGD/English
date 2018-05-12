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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;

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
    LinearLayout layoutCaiDat;
    ArrayList<String> data = new ArrayList<>();
    String arr[] = {
            "Read",
            "Answer question"};
    int spinnerindex;
    ImageView setting;
    boolean opensetting;
    Button choosefile;
    TextView txtFile;
    Data_Model data_model;
    DATA getDataaa;
    EditText edtSoCau, edtTG;
    CheckBox checkAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myLayout = (RelativeLayout) findViewById(R.id.myLayout);
        animationDrawable = (AnimationDrawable) myLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();
        spinnerindex = 0;
        opensetting = false;
        data_model = new Data_Model();
        getDataaa = new DATA(MainActivity.this);

        createDB();
        data_model = getDataaa.getData();
        edtSoCau = findViewById(R.id.edtSoCau);
        checkAuto = findViewById(R.id.checkAuto);
        edtTG = findViewById(R.id.edtTG);
        ckbRemember = (CheckBox) findViewById(R.id.ckbRemember);


        choosefile = findViewById(R.id.choosefile);
        txtFile = findViewById(R.id.txtFile);
        setting = (ImageView) findViewById(R.id.setting);
        layoutCaiDat = findViewById(R.id.layoutCaiDat);
        final Spinner spnType = (Spinner) findViewById(R.id.spnType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnType.setAdapter(adapter);
        spnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerindex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);

        }

        if (data_model.getCheck() == 1) {
            ckbRemember.setChecked(true);

            txtFile.setText(data_model.getLink());
            if (data_model.getType() == 1) {
                spinnerindex = 0;
                spnType.setSelection(0);
            } else {
                spinnerindex = 1;
                spnType.setSelection(1);
            }
            edtSoCau.setText(data_model.getSentence());
            edtTG.setText(data_model.getTime());
            if (data_model.getAuto() == 1) {
                checkAuto.setChecked(true);
            }

        } else {
            ckbRemember.setChecked(false);
        }


        btnStart = (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                if (edtSoCau.getText().equals("0")) {
                    Toast.makeText(getApplicationContext(), "Chọn số câu muốn học", Toast.LENGTH_SHORT).show();
                } else {
                    if (edtTG.getText().equals("0")) {
                        Toast.makeText(getApplicationContext(), "Chọn thời gian cho 1 câu muốn học", Toast.LENGTH_SHORT).show();
                    } else {
                        if (data_model.getLink().equals("")) {
                            chooserFile();

                        } else {
                            checkkkk();
                        }
                    }
                }
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (opensetting) {
                    layoutCaiDat.setVisibility(View.GONE);
                } else {
                    layoutCaiDat.setVisibility(View.VISIBLE);
                }
                opensetting = !opensetting;
            }
        });

        choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooserFile2();
            }
        });

        ckbRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    showAlertDialog();
                }
            }
        });
    }

    private void checkkkk() {
        if (ckbRemember.isChecked()) {

            data_model.setTime(edtTG.getText().toString());
            data_model.setSentence(edtSoCau.getText().toString());
            data_model.setType(spinnerindex + 1);
            if (checkAuto.isChecked()) {
                data_model.setAuto(1);
            } else {
                data_model.setAuto(0);
            }
            data_model.setCheck(1);
            getDataaa.saveData(data_model);
                                /*if (data_model.getType() == 1) {
                                    startLearning(1);
                                } else {
                                    startLearning(2);
                                }*/
            // SAVEEEEEEEEEEEEEE
            startLearning(data_model.getType());
        } else {
            // REMOVEEEEEEEEEEEEEEEEe

            data_model.setTime(edtTG.getText().toString());
            data_model.setSentence(edtSoCau.getText().toString());
            data_model.setType(spinnerindex + 1);
            if (checkAuto.isChecked()) {
                data_model.setAuto(1);
            } else {
                data_model.setAuto(0);
            }


            Data_Model temp = new Data_Model();
            temp.setLink("");
            temp.setCheck(0);
            temp.setType(0);
            temp.setAuto(0);
            temp.setSentence("0");
            temp.setTime("0");
            getDataaa.saveData(temp);

                                /*if (data_model.getType() == 1) {
                                    startLearning(1);
                                } else {
                                    startLearning(2);
                                }
                                startLearning(spinnerindex + 1);*/
            startLearning(data_model.getType());
        }
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
                        //clearPath();

                        Data_Model temp = new Data_Model();
                        temp.setLink("");
                        temp.setCheck(0);
                        temp.setType(0);
                        temp.setAuto(0);
                        temp.setSentence("0");
                        temp.setTime("0");
                        getDataaa.saveData(temp);

                        edtSoCau.setText("");
                        edtTG.setText("");
                        checkAuto.setChecked(true);
                        txtFile.setText("");

                        data_model.setTime(edtTG.getText().toString());
                        data_model.setSentence(edtSoCau.getText().toString());
                        data_model.setType(spinnerindex + 1);
                        if (checkAuto.isChecked()) {
                            data_model.setAuto(1);
                        } else {
                            data_model.setAuto(0);
                        }

                        data_model.setLink("");
                    }
                });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancel the alert box and put a Toast to the user
                        dialog.cancel();
                        ckbRemember.setChecked(true);
                    }
                });

        AlertDialog alertDialog =
                alertDialogBuilder.create();

        alertDialog.setCancelable(false);
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

    private void chooserFile2() {
        new MaterialFilePicker()
                .withActivity(this)
                //.withFilter(Pattern.compile(".*\\.txt$"))
                .withRequestCode(1001)
                .start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            /*if (ckbRemember.isChecked()) {
                dataPath = filePath;
                savePath();

            }*/
            data_model.setLink(filePath);
            txtFile.setText(filePath);
            checkkkk();
            //StringBuilder text = getdata(filePath);
            //Toast.makeText(getApplication(), text, Toast.LENGTH_SHORT).show();


            //startLearning();

        } else {
            if (requestCode == 1001 && resultCode == RESULT_OK) {
                String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                data_model.setLink(filePath);
                txtFile.setText(filePath);
                readData(filePath);
            }
        }
    }


    private void readData(String file) {
        StringBuilder text = new StringBuilder();
        File files = new File(file);
        try {
            BufferedReader br = new BufferedReader(new FileReader(files));
            String line;

            while ((line = br.readLine()) != null) {
                /*text.append(line);
                text.append('\n');*/
                data.add(line);
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
    }

    private StringBuilder getdata(String path) {
        data.clear();
        BufferedReader br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(path));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            String line = "";
            StringBuilder tmp = new StringBuilder();
            while ((line = br.readLine()) != null) {
                data.add(line);
            }
            return tmp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startLearning(int index) {
        /*Intent intent = new Intent(this,studyingActivity.class);
        startActivity(intent);*/
        readData(data_model.getLink());
        Bundle bundle = new Bundle();
        if (data_model.getType() == 1) {
            bundle.putStringArrayList("data", data);
        } else {
            ArrayList<Answer_Model> mdata = new ArrayList<>();

            for (int i = 0; i < data.size(); i = i + 2) {
                Answer_Model answer_model = new Answer_Model();
                answer_model.setQuestion(data.get(i));
                if (i + 1 >= data.size()) {

                } else {
                    answer_model.setAnswer(data.get(i + 1));
                }

                mdata.add(answer_model);
            }

            bundle.putSerializable("data", mdata);
        }

        bundle.putString("time", data_model.getTime());
        bundle.putInt("auto", data_model.getAuto());
        if (edtSoCau.getText().toString().equals("")) {
            bundle.putInt("socau", data.size());
        } else {
            bundle.putInt("socau", Integer.parseInt(edtSoCau.getText().toString()));
        }

        if (index == 1) {
            if (data_model.getAuto() == 1) {
                Intent intent = new Intent(MainActivity.this, Reading.class);
                intent.putExtra("data", bundle);
                startActivity(intent);
            } else {
                Intent intent = new Intent(MainActivity.this, Reading2.class);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }

        } else {
            if (index == 2) {
                if (data_model.getAuto() == 1) {
                    Intent intent = new Intent(MainActivity.this, Answer.class);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity.this, Answer2.class);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                }

            }
        }
    }

    private void createDB() {
        SQLiteDataController sql = new SQLiteDataController(this);

        try {
            sql.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
