package me.tom.jsbridgewebview;


import java.lang.ref.WeakReference;

public class JsBridgeNativeCallBack {

    private String mCallBackId;
    private WeakReference<JsBridgeWebView> mWebViewRef;

    public JsBridgeNativeCallBack(JsBridgeWebView webView, String callbackId) {
        mCallBackId = callbackId;
        mWebViewRef = new WeakReference<>(webView);
    }

    public void onCallback(Object data) {
        JsBridgeWebView webView = mWebViewRef.get();
        if (webView == null) {
            return;
        }
        if (data == null) {
            webView.loadUrl("javascript:window.jsBridgeWebView._nativeCallbackHandler(null,'" + mCallBackId + "')");
        } else {
            webView.loadUrl("javascript:window.jsBridgeWebView._nativeCallbackHandler(" + data.toString() + ",'" + mCallBackId + "')");
        }
    }
}
