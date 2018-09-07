package com.kunleen.sn.sportnewsapplication.network.bean;

import java.io.Serializable;


public class TResponse implements Serializable {

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    private int code;

    private String message;

    private boolean result;

}
