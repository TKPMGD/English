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

/**
 * Created by CongHoang on 5/2/2018.
 */

public class Result_Adapter extends ArrayAdapter {

    ArrayList<Result_Model> data = new ArrayList<>();
    private int layoutResouce;
    Activity context;

    public Result_Adapter(@NonNull Activity context, int resource, ArrayList<Result_Model> data) {
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
        v = inflater.inflate(R.layout.item_list_result, null);


        Result_Model bl = new Result_Model();
        bl = data.get(position);
        TextView id = (TextView) v.findViewById(R.id.id);
        TextView nddung = (TextView) v.findViewById(R.id.nddung);
        TextView nddoc = (TextView) v.findViewById(R.id.nddoc);
        ImageView dungsai = (ImageView) v.findViewById(R.id.dungsai);
        ImageView again = (ImageView) v.findViewById(R.id.again);


        id.setText(String.valueOf(bl.getStt()));
        nddung.setText(bl.getNd());


        if (bl.isResult()) {
            dungsai.setImageResource(R.drawable.tick);
            nddoc.setVisibility(View.GONE);
        } else {
            dungsai.setImageResource(R.drawable.incorrect);
            nddoc.setText(bl.getNddoc());
        }

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Read_Again.class);
                Bundle bundle = new Bundle();
                bundle.putInt("time", data.get(position).getTime());
                bundle.putString("nd", data.get(position).getNd());
                bundle.putInt("index", position);

                intent.putExtra("data", bundle);
                context.startActivityForResult(intent, 19);
            }
        });

        return v;
    }
}
