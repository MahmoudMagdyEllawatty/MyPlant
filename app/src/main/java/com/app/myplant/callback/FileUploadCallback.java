package com.app.myplant.callback;

public interface FileUploadCallback {
    void onSuccess(String url);
    void onFail(String msg);
}
