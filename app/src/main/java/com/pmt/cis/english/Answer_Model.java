package com.pmt.cis.english;

import java.io.Serializable;

/**
 * Created by CongHoang on 5/8/2018.
 */

public class Answer_Model implements Serializable {
    private String question;
    private String nddoc;

    public String getNddoc() {
        return nddoc;
    }

    public void setNddoc(String nddoc) {
        this.nddoc = nddoc;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String answer;
    int stt;
    boolean result;
    int time;

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
