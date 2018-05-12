package com.pmt.cis.english;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    ArrayList<Result_Model> lstResult;
    ArrayList<Answer_Model> answer_models;
    Result_Adapter result_adapter;
    Answer_Result_Adapter answer_result_adapter;
    ListView lstKQ;
    int key;
    TextView txtContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lstResult = new ArrayList<>();
        answer_models = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        key = bundle.getInt("key");
        lstKQ = findViewById(R.id.lstKQ);
        if (key == 1) {

            lstResult = (ArrayList<Result_Model>) bundle.getSerializable("lstresult");
            result_adapter = new Result_Adapter(Result.this, R.layout.item_list_result, lstResult);

            lstKQ.setAdapter(result_adapter);
        } else {
            if (key == 2) {
                answer_models = (ArrayList<Answer_Model>) bundle.getSerializable("data");
                answer_result_adapter = new Answer_Result_Adapter(Result.this, R.layout.item_result_answer, answer_models);
                lstKQ.setAdapter(answer_result_adapter);
            }
        }


        txtContinue = findViewById(R.id.txtContinue);

        txtContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putInt("iscontinue", 1);
                intent.putExtra("data", bundle);
                setResult(22, intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (key == 1) {
                Bundle bundle = data.getBundleExtra("data");
                int pos = bundle.getInt("index");

                String nd = bundle.getString("nddoc");
                if (nd.length() > 0) {
                    nd = nd.substring(0, nd.length() - 1);
                }
                if (lstResult.get(pos).getNd().toLowerCase().equals(nd.toLowerCase())) {
                    lstResult.get(pos).setResult(true);
                } else {
                    lstResult.get(pos).setResult(false);
                    lstResult.get(pos).setNddoc(nd);
                }

                result_adapter.notifyDataSetChanged();
            } else {
                Bundle bundle = data.getBundleExtra("data");
                int pos = bundle.getInt("index");

                String nd = bundle.getString("nddoc");
                if (nd.length() > 0) {
                    nd = nd.substring(0, nd.length() - 1);
                }
                if (answer_models.get(pos).getAnswer().toLowerCase().equals(nd.toLowerCase())) {
                    answer_models.get(pos).setResult(true);
                } else {
                    answer_models.get(pos).setResult(false);
                    answer_models.get(pos).setNddoc(nd);
                }

                answer_result_adapter.notifyDataSetChanged();
            }

        }
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
