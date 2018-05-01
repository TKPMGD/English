package com.pmt.cis.english;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * Created by CongHoang on 5/1/2018.
 */

public class DATA extends SQLiteDataController {
    public DATA(Context con) {
        super(con);
    }

    public Data_Model getData() {
        Data_Model data_model = new Data_Model();
        try {
            openDataBase();
            Cursor cs = database.rawQuery("select * from data", null);
            while (cs.moveToNext()) {
                data_model.setAuto(cs.getInt(4));
                data_model.setLink(cs.getString(0));
                data_model.setSentence(cs.getString(2));
                data_model.setTime(cs.getString(3));
                data_model.setType(cs.getInt(1));
                data_model.setCheck(cs.getInt(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return data_model;
    }

    public void saveData(Data_Model data_model) {
        try {
            openDataBase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("link", data_model.getLink().toString());
            contentValues.put("type", data_model.getType());
            contentValues.put("sentences", data_model.getSentence().toString());
            contentValues.put("time", data_model.getTime().toString());
            contentValues.put("auto", data_model.getAuto());
            contentValues.put("check_re", data_model.getCheck());
            /*database.delete("data", null, null);*/
            int rs = database.update("data", contentValues, "id=1", null);

            //database.execSQL("update data set check = "+ data_model.getCheck()+", link = '"+data_model.getLink()+"', type = "+data_model.getType()+", sentences = '"+data_model.getSentence()+"', time = '"+data_model.getTime()+"',auto = "+data_model.getAuto()+"");
            //database.beginTransaction();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void delete() {
        try {
            openDataBase();
            database.delete("data", null, null);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
