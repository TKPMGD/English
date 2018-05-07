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
    Result_Adapter result_adapter;
    ListView lstKQ;

    TextView txtContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lstResult = new ArrayList<>();
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        lstKQ = findViewById(R.id.lstKQ);
        lstResult = (ArrayList<Result_Model>) bundle.getSerializable("lstresult");
        result_adapter = new Result_Adapter(Result.this, R.layout.item_list_result, lstResult);

        lstKQ.setAdapter(result_adapter);
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
            Bundle bundle = data.getBundleExtra("data");
            int pos = bundle.getInt("index");

            String nd = bundle.getString("nddoc");
            if (nd.length() > 0) {
                nd = nd.substring(0, nd.length() - 1);
            }
            if (lstResult.get(pos).getNd().equals(nd)) {
                lstResult.get(pos).setResult(true);
            } else {
                lstResult.get(pos).setResult(false);
                lstResult.get(pos).setNddoc(nd);
            }

            result_adapter.notifyDataSetChanged();
        }
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
