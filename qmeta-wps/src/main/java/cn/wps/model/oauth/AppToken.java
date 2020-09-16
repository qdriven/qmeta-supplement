package cn.wps.model.oauth;

import org.json.JSONObject;

public class AppToken {
    public String appToken;
    public long expiresIn;

    public static AppToken fromJsonObject(JSONObject jsonObject)
    {
        AppToken appToken = new AppToken();
        appToken.appToken = jsonObject.optString("app_token");
        appToken.expiresIn = jsonObject.optLong("expires_in");
        return appToken;
    }
}
