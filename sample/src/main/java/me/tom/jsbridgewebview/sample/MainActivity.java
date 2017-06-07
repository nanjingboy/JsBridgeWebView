package me.tom.jsbridgewebview.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.tom.jsbridgewebview.JsBridgeCallBack;
import me.tom.jsbridgewebview.JsBridgeHandler;
import me.tom.jsbridgewebview.JsBridgeWebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JsBridgeWebView webView = (JsBridgeWebView) findViewById(R.id.webView);
        try {
            webView.loadDataWithBaseURL(null, getContent(), "text/html", "UTF-8", null);
        } catch (Exception e) {
        }
        webView.registerHandler("callNative", new JsBridgeHandler() {
            @Override
            public void handler(JSONObject data, JsBridgeCallBack callBack) {
                callBack.onCallback("Response from native");
            }
        });
    }

    private String getContent() {
        String content = "";
        try {
            InputStream inputStream = getAssets().open("demo.html");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte buf[] = new byte[1024];
            int length;
            while ((length = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, length);
            }
            outputStream.close();
            inputStream.close();
            content = outputStream.toString();
        } catch (IOException e) {
        }
        return content;
    }
}