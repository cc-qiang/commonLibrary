package com.chen.library.http;


public interface HttpResponseListener {
    void onHttpResponseSuccessful(String response);

    void onHttpResponseFailed(Throwable e, String errorMessage, String errorCode);

    void onHttpResponseComplete();
}
