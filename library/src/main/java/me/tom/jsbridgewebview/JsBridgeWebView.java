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

    private HashMap<String, JsBridgeHandler> mHandlers;

    public JsBridgeWebView(Context context) {
        this(context, null);
    }

    public JsBridgeWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JsBridgeWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mHandlers = new HashMap<>();
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
                            if (mHandlers.containsKey(name)) {
                                JsBridgeCallBack callBack = new JsBridgeCallBack(
                                        JsBridgeWebView.this,
                                        options.getString("callbackId")
                                );
                                if (options.isNull("arguments")) {
                                    mHandlers.get(name).handler(null, callBack);
                                } else {
                                    mHandlers.get(name).handler(options.getJSONObject("arguments"), callBack);
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
            }
        }, "jsBridgeWebViewInterface");
    }

    public void registerHandler(String name, JsBridgeHandler handler) {
        mHandlers.put(name, handler);
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
