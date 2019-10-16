package me.tom.jsbridgewebview;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class JsBridgeWebView extends WebView {

    private long mPreTouchTime;

    private HashMap<String, JsBridgeNativeHandler> mNativeHandlers;
    private HashMap<String, JsBridgeJsCallbackHandler> mJsCallbackHandlers;

    public JsBridgeWebView(Context context) {
        super(context);
        init();
    }

    public JsBridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public JsBridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mNativeHandlers = new HashMap<>();
        mJsCallbackHandlers = new HashMap<>();
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setUseWideViewPort(true);
        getSettings().setDefaultTextEncodingName("UTF-8");

        setWebChromeClient(new JsBridgeWeChromeClient());
        setWebViewClient(new JsBridgeWebViewClient(this));
        addJavascriptInterface(new JsBridgeJavaScriptInterface() {
            @Override
            @JavascriptInterface
            public void callNativeHandler(final String data) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject options = new JSONObject(data);
                            String name = options.getString("name");
                            if (mNativeHandlers.containsKey(name)) {
                                JsBridgeNativeCallBack callBack = new JsBridgeNativeCallBack(
                                        JsBridgeWebView.this,
                                        options.getString("callbackId")
                                );
                                mNativeHandlers.get(name).handler(options.get("arguments"), callBack);

                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }

            @Override
            @JavascriptInterface
            public void jsCallbackHandler(final String data) {
                ((Activity) getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject options = new JSONObject(data);
                            String name = options.getString("name");
                            if (mJsCallbackHandlers.containsKey(name)) {
                                if (options.isNull("response")) {
                                    mJsCallbackHandlers.get(name).handler(null);
                                } else {
                                    mJsCallbackHandlers.get(name).handler(options.getJSONObject("response"));
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        }, "jsBridgeWebViewInterface");
    }

    public void registerHandler(String name, JsBridgeNativeHandler handler) {
        mNativeHandlers.put(name, handler);
    }

    public void callHandler(String name, Object data, JsBridgeJsCallbackHandler handler) {
        mJsCallbackHandlers.put(name, handler);
        if (data == null) {
            loadUrl("javascript:window.jsBridgeWebView._callJsHandler('" + name + "',null)");
        } else {
            loadUrl("javascript:window.jsBridgeWebView._callJsHandler('" + name + "','" + data.toString() + "')");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            long currentTouchTime = System.currentTimeMillis();
            if (currentTouchTime - mPreTouchTime <= ViewConfiguration.getDoubleTapTimeout()) {
                mPreTouchTime = currentTouchTime;
                return true;
            }
            mPreTouchTime = currentTouchTime;
        }
        return super.onTouchEvent(event);
    }
}
