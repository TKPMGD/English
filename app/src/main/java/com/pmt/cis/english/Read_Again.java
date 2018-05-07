package com.pmt.cis.english;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Read_Again extends AppCompatActivity {
    LinearLayout layoutOverlay;
    TextView txtKQ, txtTGCB, textTGConLai, txtCau;
    private SpeechRecognizerManager mSpeechManager;
    String cau = "";
    int index, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read__again);
        getSupportActionBar().setTitle("Read Again");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutOverlay = findViewById(R.id.layoutOverlay);
        txtTGCB = findViewById(R.id.txtTGCB);
        textTGConLai = findViewById(R.id.textTGConLai);
        txtCau = findViewById(R.id.txtCau);
        txtKQ = findViewById(R.id.txtKQ);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        cau = bundle.getString("nd");
        index = bundle.getInt("index");
        time = bundle.getInt("time");
        textTGConLai.setText(String.valueOf(time));
        layoutOverlay.setVisibility(View.VISIBLE);
        //Toast.makeText(getApplicationContext(), bundle.getString("nd"), Toast.LENGTH_SHORT).show();

        new CountDownTimer(2000, 1) {

            public void onTick(long millisUntilFinished) {
                txtTGCB.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                layoutOverlay.setVisibility(View.GONE);
                txtKQ.setVisibility(View.VISIBLE);
                        /*while (intcauhientai <= socau){*/
                txtKQ.setText("");
                starRecording(time);
                //}

            }
        }.start();

    }

    private void SetSpeechListener() {
        mSpeechManager = new SpeechRecognizerManager(this, new SpeechRecognizerManager.onResultsReady() {
            @Override
            public void onResults(ArrayList<String> results) {
                if (results != null && results.size() > 0) {
                    txtKQ.append(results.get(0) + " ");
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

        txtCau.setText(cau);
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
}
