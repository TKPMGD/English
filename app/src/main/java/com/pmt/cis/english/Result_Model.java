package com.pmt.cis.english;

import java.io.Serializable;

/**
 * Created by CongHoang on 5/2/2018.
 */

public class Result_Model implements Serializable {
    int stt;
    String nd;
    String nddoc;
    boolean result;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getNddoc() {
        return nddoc;
    }

    public void setNddoc(String nddoc) {
        this.nddoc = nddoc;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
