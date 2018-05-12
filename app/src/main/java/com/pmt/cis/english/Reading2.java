package com.pmt.cis.english;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Reading2 extends AppCompatActivity {


    int socau;
    int intcauhientai = 0;
    int time = 20;
    int auto = 1;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<Result_Model> resultss = new ArrayList<>();
    TextView txtKQ, txtTGCB, textTGConLai, cauhientai, txtCau;
    LinearLayout layoutStart, layoutOverlay, layoutTG;
    LinearLayout layoutAgain;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    Button btnNext;
    private SpeechRecognizerManager mSpeechManager;
    boolean isrepeat;
    String texxt = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading2);
        getSupportActionBar().setTitle("Reading");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutAgain = findViewById(R.id.layoutAgain);
        btnNext = findViewById(R.id.btnNext);
        textTGConLai = findViewById(R.id.textTGConLai);
        cauhientai = findViewById(R.id.cauhientai);
        txtKQ = findViewById(R.id.txtKQ);
        layoutOverlay = findViewById(R.id.layoutOverlay);
        layoutTG = findViewById(R.id.layoutTG);
        layoutStart = findViewById(R.id.layoutStart);
        txtCau = findViewById(R.id.txtCau);
        txtTGCB = findViewById(R.id.txtTGCB);
        getData();
        isrepeat = false;

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

        //int sss = 1;

        layoutAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isrepeat = true;
                starRecording(time);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intcauhientai + 1 < socau) {
                    intcauhientai = intcauhientai + 1;
                    starRecording(time);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("lstresult", (Serializable) resultss);
                    bundle.putInt("key", 1);

                    Intent intent = new Intent(Reading2.this, Result.class);
                    intent.putExtra("data", bundle);
                    startActivityForResult(intent, 1997);
                    int ssss = 0;
                }
            }
        });
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
            for (i = 0; i < array1.length; i++) {
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

            for (i = i; i < array2.length; i++) {
                incorrect = incorrect + array2[i] + " ";
            }

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
                } else {
                    //txtKQ.setText("KO KQ");
                }

            }
        });
    }

    public void starRecording(int giay) {
        txtKQ.setText("");
        texxt = "";
        layoutTG.setVisibility(View.VISIBLE);
        layoutAgain.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
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


                    if (isrepeat) {
                        resultss.remove(resultss.size() - 1);
                        resultss.add(result_model);
                    } else {
                        resultss.add(result_model);
                    }

                    mSpeechManager.destroy();
                    mSpeechManager = null;
                    layoutTG.setVisibility(View.GONE);
                    layoutAgain.setVisibility(View.VISIBLE);

                    btnNext.setVisibility(View.VISIBLE);
                    isrepeat = false;
                }
            }
        }.start();
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
                    layoutAgain.setVisibility(View.GONE);
                    layoutTG.setVisibility(View.VISIBLE);
                    txtKQ.setText("");
                    texxt = "";
                    txtKQ.setVisibility(View.GONE);
                    layoutStart.setVisibility(View.VISIBLE);
                    intcauhientai = 0;
                    isrepeat = false;
                    btnNext.setVisibility(View.GONE);
                    getData();
                }
            }
        }
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
