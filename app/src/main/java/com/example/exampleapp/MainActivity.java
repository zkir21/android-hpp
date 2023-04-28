package com.example.exampleapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    public static final String UNIPAY = "Unipay";
    private WebView myWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView =(WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl("https://unipay:8443/gates/xurl?requestType=sale&accountId=3001&accountType=R&...");

        myWebView.addJavascriptInterface(new JavaScriptInterface(this), "callback");

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public class JavaScriptInterface {

        private static final String AMPERSAND = "&";
        private static final String EQUALS = "=";


        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void process(String data) {
            Map<String, String> response = Arrays.asList(data.split(AMPERSAND))
                    .stream()
                    .map(p -> p.split(EQUALS, 2))
                    .filter(e -> e.length == 2)
                    .collect(Collectors.toMap(e -> e[0], e -> e[1], (existing, replacement) -> existing, LinkedHashMap::new));
            Log.i(UNIPAY, response.toString());
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

}
