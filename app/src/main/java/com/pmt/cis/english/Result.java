package com.pmt.cis.english;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class Result extends AppCompatActivity {

    ArrayList<Result_Model> lstResult;
    Result_Adapter result_adapter;
    ListView lstKQ;

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
        result_adapter = new Result_Adapter(getApplicationContext(), R.layout.item_list_result, lstResult);

        lstKQ.setAdapter(result_adapter);


    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
