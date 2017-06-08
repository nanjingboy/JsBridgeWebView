package me.tom.jsbridgewebview;

import org.json.JSONObject;

public interface JsBridgeNativeHandler {

    void handler(JSONObject data, JsBridgeNativeCallBack callBack);
}
