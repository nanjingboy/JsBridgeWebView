package me.tom.jsbridgewebview;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class JsBridgeCallBack {

    private String mCallBackId;
    private WeakReference<JsBridgeWebView> mWebViewRef;

    public JsBridgeCallBack(JsBridgeWebView webView, String callbackId) {
        mCallBackId = callbackId;
        mWebViewRef = new WeakReference<>(webView);
    }

    public void onCallback(JSONObject data) {
        JsBridgeWebView webView = mWebViewRef.get();
        if (webView == null) {
            return;
        }
        if (data == null) {
            data = new JSONObject();
        }
        webView.loadUrl("javascript:window.jsBridgeWebView.callbackHandler('" + data.toString() + "','" + mCallBackId + "')");
    }
}
