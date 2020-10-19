package cn.wps.model.oauth;

import org.json.JSONObject;

public class Token {
    public String appid;
    public long expiresIn;
    public String accessToken;
    public String refreshToken;
    public String openid;

    public static Token fromJsonObject(JSONObject jsonObject)
    {
        JSONObject tokenObject = jsonObject.optJSONObject("token");
        if (tokenObject == null)
            return null;
        Token token = new Token();
        token.appid = tokenObject.optString("appid");
        token.accessToken = tokenObject.optString("access_token");
        token.refreshToken = tokenObject.optString("refresh_token");
        token.openid = tokenObject.optString("openid");
        token.expiresIn = tokenObject.optLong("expires_in");
        return token;
    }
}
