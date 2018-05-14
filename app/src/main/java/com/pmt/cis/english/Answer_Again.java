package com.pmt.cis.english;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Answer_Again extends AppCompatActivity {

    TextView txtKQ, txtTGCB, textTGConLai, Cauhoi;
    LinearLayout layoutStart, layoutOverlay;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;

    private SpeechRecognizerManager mSpeechManager;

    String cauhoi, cautl;
    int index;
    int time;
    String texxt = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer__again);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Answer");

        Cauhoi = findViewById(R.id.Cauhoi);
        txtTGCB = findViewById(R.id.txtTGCB);
        textTGConLai = findViewById(R.id.textTGConLai);
        txtKQ = findViewById(R.id.txtKQ);
        layoutOverlay = findViewById(R.id.layoutOverlay);
        layoutStart = findViewById(R.id.layoutStart);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        cauhoi = bundle.getString("nd");
        cautl = bundle.getString("tl");
        index = bundle.getInt("index");
        time = bundle.getInt("time");
        textTGConLai.setText(String.valueOf(time));
        layoutOverlay.setVisibility(View.VISIBLE);

        new CountDownTimer(2000, 1) {

            public void onTick(long millisUntilFinished) {
                txtTGCB.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                layoutOverlay.setVisibility(View.GONE);
                txtKQ.setVisibility(View.VISIBLE);
                        /*while (intcauhientai <= socau){*/
                txtKQ.setText("");
                texxt = "";
                starRecording(time);
                //}

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
                    txtKQ.setText(Html.fromHtml(getResult(cautl, texxt)), TextView.BufferType.SPANNABLE);
                } else {
                    //txtKQ.setText("KO KQ");
                }

            }
        });
    }

    public void starRecording(int giay) {
        if (mSpeechManager == null) {
            SetSpeechListener();
        } else if (!mSpeechManager.ismIsListening()) {
            mSpeechManager.destroy();
            SetSpeechListener();
        }

        Cauhoi.setText(cauhoi);
        new CountDownTimer(giay * 1000 + 1000, 1) {

            public void onTick(long millisUntilFinished) {
                // speechRecognizer.
                textTGConLai.setText(String.valueOf(millisUntilFinished / 1000));
                // myService.mSpeechRecognizer.
            }

            public void onFinish() {
                if (mSpeechManager != null) {
                    mSpeechManager.destroy();
                    mSpeechManager = null;
                }

                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putInt("index", index);
                bundle.putString("nddoc", txtKQ.getText().toString());
                intent.putExtra("data", bundle);
                setResult(2, intent); // phương thức này sẽ trả kết quả cho Activity1
                finish(); // Đóng Activity hiện tại
            }
        }.start();
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
