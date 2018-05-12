package com.pmt.cis.english;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Answer_Result_Adapter extends ArrayAdapter {
    ArrayList<Answer_Model> data = new ArrayList<>();
    private int layoutResouce;
    Activity context;

    Answer_Result_Adapter(@NonNull Activity context, int resource, ArrayList<Answer_Model> data) {
        super(context, resource);
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_result_answer, null);


        Answer_Model answer = new Answer_Model();
        answer = data.get(position);

        TextView id = (TextView) v.findViewById(R.id.id);
        TextView nddung = (TextView) v.findViewById(R.id.nddung);
        TextView ndtl = (TextView) v.findViewById(R.id.ndtl);
        TextView nddoc = (TextView) v.findViewById(R.id.nddoc);
        ImageView dungsai = (ImageView) v.findViewById(R.id.dungsai);
        ImageView again = (ImageView) v.findViewById(R.id.again);
        ImageView arrow2 = (ImageView) v.findViewById(R.id.arrow2);

        id.setText(String.valueOf(answer.getStt()));
        nddung.setText(answer.getQuestion());
        ndtl.setText(answer.getAnswer());

        if (answer.isResult()) {
            dungsai.setImageResource(R.drawable.tick);
            nddoc.setVisibility(View.GONE);
            arrow2.setVisibility(View.GONE);
        } else {
            dungsai.setImageResource(R.drawable.incorrect);
            nddoc.setText(answer.getNddoc());
            nddoc.setVisibility(View.VISIBLE);
            arrow2.setVisibility(View.VISIBLE);
        }

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Answer_Again.class);
                Bundle bundle = new Bundle();
                bundle.putInt("time", data.get(position).getTime());
                bundle.putString("nd", data.get(position).getQuestion());
                bundle.putString("tl", data.get(position).getAnswer());
                bundle.putInt("index", position);

                intent.putExtra("data", bundle);
                context.startActivityForResult(intent, 199);
            }
        });

        return v;
    }
}
