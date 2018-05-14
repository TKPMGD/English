package com.pmt.cis.english;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class Reading extends AppCompatActivity {

    int socau;
    int intcauhientai = 0;
    int time = 20;
    int auto = 1;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<Result_Model> resultss = new ArrayList<>();
    TextView txtKQ, txtTGCB, textTGConLai, cauhientai, txtCau;
    LinearLayout layoutStart, layoutOverlay;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    String texxt = "";
    private SpeechRecognizerManager mSpeechManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        getSupportActionBar().setTitle("Reading");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textTGConLai = findViewById(R.id.textTGConLai);
        cauhientai = findViewById(R.id.cauhientai);
        txtKQ = findViewById(R.id.txtKQ);
        layoutOverlay = findViewById(R.id.layoutOverlay);
        layoutStart = findViewById(R.id.layoutStart);
        txtCau = findViewById(R.id.txtCau);
        txtTGCB = findViewById(R.id.txtTGCB);
        getData();


        layoutStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutOverlay.setVisibility(View.VISIBLE);
                layoutStart.setVisibility(View.GONE);

                new CountDownTimer(4000, 1) {

                    public void onTick(long millisUntilFinished) {
                        txtTGCB.setText(String.valueOf(millisUntilFinished / 1000));
                    }

                    public void onFinish() {
                        layoutOverlay.setVisibility(View.GONE);
                        txtKQ.setVisibility(View.VISIBLE);
                        cauhientai.setText("1/" + String.valueOf(socau));
                        /*while (intcauhientai <= socau){*/
                        txtKQ.setText("");
                        texxt = "";
                        starRecording(time);
                        //}
                    }
                }.start();
            }
        });
    }

    public void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        if (!bundle.getString("time").equals("")) {
            time = Integer.parseInt(bundle.getString("time"));
        }
        auto = bundle.getInt("auto");
        socau = bundle.getInt("socau");
        ArrayList<String> temp = new ArrayList<>();
        temp = bundle.getStringArrayList("data");

        textTGConLai.setText(String.valueOf(time));

        if (temp.size() < socau) {
            socau = temp.size();
        }
        cauhientai.setText("0/" + String.valueOf(socau));
        ArrayList<Integer> tss = new ArrayList<>();
        for (int i = 0; i < socau; i++) {
            Random rd = new Random();
            int number1 = rd.nextInt(temp.size());
            while (tss.contains(number1)) {
                number1 = rd.nextInt(temp.size());
            }

            tss.add(number1);
            data.add(temp.get(number1));
        }

        //int sss = 1;
    }

    public void starRecording(int giay) {
        if (mSpeechManager == null) {
            SetSpeechListener();
        } else if (!mSpeechManager.ismIsListening()) {
            mSpeechManager.destroy();
            SetSpeechListener();
        }
        cauhientai.setText(String.valueOf(intcauhientai + 1) + "/" + String.valueOf(socau));
        txtCau.setText(data.get(intcauhientai));
        new CountDownTimer(giay * 1000 + 1000, 1) {

            public void onTick(long millisUntilFinished) {
                // speechRecognizer.
                textTGConLai.setText(String.valueOf(millisUntilFinished / 1000));
                // myService.mSpeechRecognizer.
            }

            public void onFinish() {
                if (mSpeechManager != null) {

                    Result_Model result_model = new Result_Model();
                    result_model.setTime(time);
                    result_model.setStt(intcauhientai + 1);
                    result_model.setNd(data.get(intcauhientai));
                    result_model.setNddoc(txtKQ.getText().toString());
                    if (result_model.getNddoc().length() > 1) {
                        result_model.setNddoc(result_model.getNddoc().substring(0, result_model.getNddoc().length() - 1));
                    }
                    if (!result_model.getNd().toLowerCase().equals(result_model.getNddoc().toLowerCase())) {
                        result_model.setResult(false);
                    } else {
                        result_model.setResult(true);
                    }

                    resultss.add(result_model);
                    intcauhientai = intcauhientai + 1;
                    mSpeechManager.destroy();
                    mSpeechManager = null;
                    next();
                }
            }
        }.start();
    }

    public String getResult(String rs, String text) {
        String rss = "";
        String[] array1 = rs.split(" ", -1);
        String[] array2 = text.split(" ", -1);

        if (array1.length < array2.length) {
            boolean is = true;
            String correct = "";
            String incorrect = "";
            for (int i = 0; i < array2.length; i++) {
                if (i < array1.length) {
                    if (is) {
                        if (array2[i].toLowerCase().equals(array1[i].toLowerCase())) {
                            correct = correct + array2[i] + " ";
                        } else {
                            incorrect = array2[i] + " ";
                            is = false;
                        }
                    } else {
                        incorrect = incorrect + array2[i] + " ";
                    }
                } else {
                    incorrect = incorrect + array2[i] + " ";
                }
            }

            rss = "<font color='green'>" + correct + "</font>" + "<font color='red'>" + incorrect + "</font>";
            return rss;
        } else {
            boolean is = true;
            String correct = "";
           String incorrect = "";
            int i;
            for (i = 0; i < array2.length; i++) {
                if (is) {
                    if (array2[i].toLowerCase().equals(array1[i].toLowerCase())) {
                        correct = correct + array2[i] + " ";
                    } else {
                        incorrect = array2[i] + " ";
                        is = false;
                    }
                } else {
                    incorrect = incorrect + array2[i] + " ";
                }
            }

            /*for (i = i; i < array2.length; i++) {
                incorrect = incorrect + array2[i] + " ";
            }*/

            rss = "<font color='green'>" + correct + "</font>" + "<font color='red'>" + incorrect + "</font>";
            return rss;
        }
    }


    private void SetSpeechListener() {
        mSpeechManager = new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {
                if (results != null && results.size() > 0) {

                    texxt = texxt + results.get(0) + " ";
                    txtKQ.setText(Html.fromHtml(getResult(data.get(intcauhientai), texxt)), TextView.BufferType.SPANNABLE);

                    /*String tmp = results.get(0);
                    String text = "This is <font color='red'>red</font>. This is <font color='blue'>blue</font>.";
                    txtKQ.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);

                    kqcuoi =
                            txtKQ.append(results.get(0) + " ");*/

                } else {
                    //txtKQ.setText("KO KQ");
                }

            }
        });
    }

    public void next() {
        if (intcauhientai < socau) {
            new CountDownTimer(500, 1) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    texxt = "";
                    txtKQ.setText("");
                    starRecording(time);
                }
            }.start();
        } else {
            new CountDownTimer(500, 1) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("lstresult", (Serializable) resultss);
                    bundle.putInt("key", 1);

                    Intent intent = new Intent(Reading.this, Result.class);
                    intent.putExtra("data", bundle);
                    startActivityForResult(intent, 1996);
                }
            }.start();
        }

    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onPause() {
        if (mSpeechManager != null) {
            mSpeechManager.destroy();
            mSpeechManager = null;
        }
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            finish();
        } else {
            if (resultCode == 22) {
                Bundle bundle = data.getBundleExtra("data");
                int iscontinue = bundle.getInt("iscontinue");
                if (iscontinue == 1) {
                    this.data.clear();
                    this.resultss.clear();
                    txtCau.setText("Test your skills and find out if you are ready for the test by taking a Scored Practice Test");
                    textTGConLai.setText(String.valueOf(time));
                    cauhientai.setText("0/" + socau);
                    txtKQ.setText("");
                    texxt = "";
                    txtKQ.setVisibility(View.GONE);
                    layoutStart.setVisibility(View.VISIBLE);
                    intcauhientai = 0;
                    getData();
                }
            }
        }
    }
}
