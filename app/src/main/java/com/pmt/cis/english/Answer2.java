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

public class Answer2 extends AppCompatActivity {

    int socau;
    int intcauhientai = 0;
    int time = 20;
    ArrayList<Answer_Model> data = new ArrayList<>();

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
        setContentView(R.layout.activity_answer2);
        getSupportActionBar().setTitle("Answer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findView();

        getData();

        addEvent();

    }

    public void addEvent() {
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
                    bundle.putSerializable("data", (Serializable) data);
                    bundle.putInt("key", 2);

                    Intent intent = new Intent(Answer2.this, Result.class);
                    intent.putExtra("data", bundle);
                    startActivityForResult(intent, 19966);
                }
            }
        });

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

    public void starRecording(int giay) {
        if (mSpeechManager == null) {
            SetSpeechListener();
        } else if (!mSpeechManager.ismIsListening()) {
            mSpeechManager.destroy();
            SetSpeechListener();
        }
        layoutTG.setVisibility(View.VISIBLE);
        layoutAgain.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        txtKQ.setText("");
        texxt = "";
        cauhientai.setText(String.valueOf(intcauhientai + 1) + "/" + String.valueOf(socau));
        txtCau.setText(data.get(intcauhientai).getQuestion());
        new CountDownTimer(giay * 1000 + 1000, 1) {
            public void onTick(long millisUntilFinished) {
                // speechRecognizer.
                textTGConLai.setText(String.valueOf(millisUntilFinished / 1000));
                // myService.mSpeechRecognizer.
            }

            public void onFinish() {
                if (mSpeechManager != null) {

                    if (txtKQ.getText().toString().length() > 0) {
                        data.get(intcauhientai).setNddoc(txtKQ.getText().toString().substring(0, txtKQ.getText().toString().length() - 1));
                    } else {
                        data.get(intcauhientai).setNddoc("");
                    }
                    data.get(intcauhientai).setStt(intcauhientai + 1);
                    data.get(intcauhientai).setTime(time);

                    if (data.get(intcauhientai).getNddoc().toLowerCase().equals(data.get(intcauhientai).getAnswer().toLowerCase())) {
                        data.get(intcauhientai).setResult(true);
                    } else {
                        data.get(intcauhientai).setResult(false);
                    }
                    mSpeechManager.destroy();
                    mSpeechManager = null;
                    layoutTG.setVisibility(View.GONE);
                    layoutAgain.setVisibility(View.VISIBLE);

                    btnNext.setVisibility(View.VISIBLE);
                    //isrepeat = false;
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

           /* for (i = i; i < array2.length; i++) {
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
                    txtKQ.setText(Html.fromHtml(getResult(data.get(intcauhientai).getAnswer(), texxt)), TextView.BufferType.SPANNABLE);
                } else {
                    //txtKQ.setText("KO KQ");
                }

            }
        });
    }

    public void findView() {
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
    }

    public void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        socau = bundle.getInt("socau");
        if (!bundle.getString("time").equals("")) {
            time = Integer.parseInt(bundle.getString("time"));
        }
        textTGConLai.setText(String.valueOf(time));

        ArrayList<Answer_Model> temp = new ArrayList<>();
        temp = (ArrayList<Answer_Model>) bundle.getSerializable("data");
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
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            finish();
        } else {
            if (resultCode == 22) {
                Bundle bundle = data.getBundleExtra("data");
                int iscontinue = bundle.getInt("iscontinue");
                if (iscontinue == 1) {

                    this.data.clear();
                    txtCau.setText("Answer the question !");
                    textTGConLai.setText(String.valueOf(time));
                    layoutAgain.setVisibility(View.GONE);
                    layoutTG.setVisibility(View.VISIBLE);
                    txtKQ.setVisibility(View.GONE);
                    layoutStart.setVisibility(View.VISIBLE);
                    intcauhientai = 0;
                    texxt = "";
                    cauhientai.setText("0/" + socau);
                    txtKQ.setText("");
                    txtKQ.setVisibility(View.GONE);
                    layoutStart.setVisibility(View.VISIBLE);
                    intcauhientai = 0;
                    btnNext.setVisibility(View.GONE);
                    getData();
                    //Toast.makeText(getApplicationContext(),"CONTINUE",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
