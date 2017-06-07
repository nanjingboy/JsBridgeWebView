package me.tom.jsbridgewebview;

import android.webkit.JavascriptInterface;

public interface JsBridgeJavaScriptInterface {

    @JavascriptInterface
    void callNativeHandler(String data);
}
