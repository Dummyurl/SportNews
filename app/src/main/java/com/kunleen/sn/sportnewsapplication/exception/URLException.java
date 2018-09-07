package com.kunleen.sn.sportnewsapplication.exception;

/**
 * Created by ysy on 2018/4/9.
 */

public class URLException extends RuntimeException {
    private final int mErrorCode;
    private final String mMessage;

    public URLException(int errorCode, String message) {
        mErrorCode = errorCode;
        mMessage = message;
    }

    public int getmErrorCode() {
        return mErrorCode;
    }

    public String getmMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return "TaskException{" +
                "mErrorCode=" + mErrorCode +
                ", mMessage='" + mMessage + '\'' +
                '}';
    }

}
