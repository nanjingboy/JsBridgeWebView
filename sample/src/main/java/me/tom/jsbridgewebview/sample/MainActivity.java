package me.tom.jsbridgewebview.sample;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.tom.jsbridgewebview.JsBridgeNativeCallBack;
import me.tom.jsbridgewebview.JsBridgeJsCallbackHandler;
import me.tom.jsbridgewebview.JsBridgeNativeHandler;
import me.tom.jsbridgewebview.JsBridgeWebView;

public class MainActivity extends AppCompatActivity {

    private JsBridgeWebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.webView);
        try {
            mWebView.loadDataWithBaseURL(null, getContent(), "text/html", "UTF-8", null);
        } catch (Exception e) {
        }
        mWebView.registerHandler("callNative", new JsBridgeNativeHandler() {
            @Override
            public void handler(Object data, JsBridgeNativeCallBack callBack) {
                showMessage(data.toString());
                try {
                    JSONObject response = new JSONObject();
                    response.put("message", "Response from Java");
                    callBack.onCallback(response);
                } catch (JSONException e) {
                }
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject data = new JSONObject();
                    data.put("message", "Request from Java");
                    mWebView.callHandler("callJavaScript", data, new JsBridgeJsCallbackHandler() {
                        @Override
                        public void handler(Object data) {
                            showMessage(data.toString());
                        }
                    });
                } catch (JSONException e) {
                }
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

    private void showMessage(String message) {
        Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        TextView textView = toast.getView().findViewById(android.R.id.message);
        if (textView != null) {
            textView.setGravity(Gravity.CENTER);
        }
        toast.setText(message);
        toast.setGravity(Gravity.BOTTOM, 0, 64);
        toast.show();
    }
}