package com.chen.library.http;


import com.chen.library.log.LogCat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;
public class HttpLauncher {

    private static HttpLauncher _instance;
    private static OkHttpClient client;

    private HttpLauncher() {
        client = new OkHttpClient.Builder()
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor())
                .retryOnConnectionFailure(true)
                .connectionSpecs(Collections.singletonList(getConnectionSpec()))
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(createSSLSocketFactory())
                .build();


    }

    public static HttpLauncher getInstance() {
        if (_instance == null) {
            synchronized (HttpLauncher.class) {
                if (_instance == null) {
                    _instance = new HttpLauncher();
                }
            }
        }
        return _instance;
    }


    private static ConnectionSpec getConnectionSpec() {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_0).cipherSuites(CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA).build();
        return spec;
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public void postWithJsonBody(final String url, final String param, final CallBackListener listener) {
        Request.Builder requestBuild = new Request.Builder().url(url);
        RequestBody body = createRequestBody(param);
        if (body != null) {
            requestBuild.post(body);
        }
        post(requestBuild.build(), listener);
    }

    public void postWithFormBody(final String url, final Map<String, String> param, final CallBackListener listener) {
        Request.Builder requestBuild = new Request.Builder().url(url);
        RequestBody body = createRequestBody(param, Charset.forName("UTF-8"));
        if (body != null) {
            requestBuild.post(body);
        }
        post(requestBuild.build(), listener);
    }

    private void post(final Request request, final CallBackListener listener) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) throws Exception {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        call.cancel();
                        emitter.onError(e);
                        emitter.onComplete();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        emitter.onNext(response.body().string());
                        emitter.onComplete();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        String result = s;
                        LogCat.i("result: "  + s);
                        LogCat.i("--------HttpLauncher:: " + "onNext");
                        listener.OnHttpSuccessful(result);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogCat.i("--------HttpLauncher:: " + "onError");
                        listener.OnHttpFailed(e);
                    }

                    @Override
                    public void onComplete() {
                        LogCat.i("--------HttpLauncher:: " + "onComplete");
                        listener.OnHttpComplete();
                    }
                });
    }


    private RequestBody createRequestBody(String json) {
        if (json != null) {
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=UTF-8"), json);
            return requestBody;
        } else {
            return null;
        }
    }

    private RequestBody createRequestBody(Map<String, String> param, Charset charset) {
        if (param.size() > 0) {
            FormBody.Builder body = new FormBody.Builder(charset);
            for (HashMap.Entry e : param.entrySet()) {
                String key = String.valueOf(e.getKey());
                String value = String.valueOf(e.getValue());
                LogCat.i("RequestBody |  " + key + "=" + value);
                if (key != null && value != null) {
                    body.add(key, value);
                }
            }
            return body.build();
        } else {
            return null;
        }
    }
}
