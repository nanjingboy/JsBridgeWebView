package me.tom.jsbridgewebview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class JsBridgeWebViewClient extends WebViewClient {

    private WeakReference<JsBridgeWebView> mWebViewRef;

    public JsBridgeWebViewClient(JsBridgeWebView webView) {
        mWebViewRef = new WeakReference<>(webView);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        loadLocalJs();
    }

    public void loadLocalJs() {
        JsBridgeWebView webView = mWebViewRef.get();
        if (webView == null) {
            return;
        }

        String jsContent = null;
        try {
            InputStream inputStream = webView.getContext().getAssets().open("JsBridgeWebView.js");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int length;
            while ((length = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, length);
            }
            outputStream.close();
            inputStream.close();
            jsContent = outputStream.toString();
        } catch (IOException e) {
        }
        if (jsContent == null) {
            return;
        }
        webView.loadUrl("javascript:" + jsContent);
    }
}
