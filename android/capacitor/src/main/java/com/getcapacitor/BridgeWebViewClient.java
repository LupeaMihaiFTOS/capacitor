package com.getcapacitor;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BridgeWebViewClient extends WebViewClient {

    private Bridge bridge;

    public BridgeWebViewClient(Bridge bridge) {
        this.bridge = bridge;
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        return bridge.getLocalServer().shouldInterceptRequest(request);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Uri url = request.getUrl();
        return bridge.launchIntent(url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return bridge.launchIntent(Uri.parse(url));
    }
    
    @Override
    public void onPageFinished(WebView view, String url) {
        view.evaluateJavascript("window.Capacitor", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                if ("null".equalsIgnoreCase(value)) {
                    view.evaluateJavascript(bridge.getJSInjector().getScriptString(), injected -> {});
                }
            }
        });
    }
}
