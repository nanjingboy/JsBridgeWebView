package me.tom.jsbridgewebview;

import java.lang.ref.WeakReference;

public class JsBridgeCallBack {

    private String mCallBackId;
    private WeakReference<JsBridgeWebView> mWebViewRef;

    public JsBridgeCallBack(JsBridgeWebView webView, String callbackId) {
        mCallBackId = callbackId;
        mWebViewRef = new WeakReference<>(webView);
    }

    public void onCallback(String data) {
        JsBridgeWebView webView = mWebViewRef.get();
        if (webView == null) {
            return;
        }
        if (data == null || data.length() == 0) {
            data = "";
        }
        webView.loadUrl("javascript:window.jsBridgeWebView.callbackHandler('" + data + "','" + mCallBackId + "')");
    }
}
