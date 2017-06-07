package me.tom.jsbridgewebview;

import org.json.JSONObject;

public interface JsBridgeHandler {

    void handler(JSONObject data, JsBridgeCallBack callBack);
}
