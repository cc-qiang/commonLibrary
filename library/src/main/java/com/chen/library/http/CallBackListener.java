package com.chen.library.http;

public interface CallBackListener {
    void OnHttpSuccessful(String response);

    void OnHttpFailed(Throwable e);

    void OnHttpComplete();
}
