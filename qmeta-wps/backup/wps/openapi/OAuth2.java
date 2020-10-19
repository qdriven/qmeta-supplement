package cn.wps.openapi;

import cn.wps.exception.YunException;
import cn.wps.http.JSONBuilder;
import cn.wps.http.Request;
import cn.wps.http.RequestBody;
import cn.wps.model.oauth.*;
import java.util.Date;

public class OAuth2 {
    private Token token;
    private long tokenExpires;

    public OAuth2(String code) throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/token?appid=" + Config.getAppid() + "&appkey=" + Config.getAppKey() + "&code=" + code)
                .get();
        token = Token.fromJsonObject(request.execute().getJSONObject());
        if(token != null) {
            long curDate = new Date().getTime() / 1000;
            tokenExpires = curDate + token.expiresIn;
        }
    }

//获取token(封装好刷新token的过程)
    public Token getToken() throws YunException {
        long curDate = new Date().getTime() / 1000;
        if (curDate < tokenExpires) {
            return token;
        }
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("appkey", Config.getAppKey());
        jsonBuilder.put("refresh_token", token.refreshToken);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/token/refresh")
                .post(new RequestBody(jsonBuilder));
        token = Token.fromJsonObject(request.execute().getJSONObject());
        if(token != null) {
            tokenExpires = curDate + token.expiresIn;
            return token;
        }

        return null;
    }

//通过code获取access_token
    public static Token getToken(String code) throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/token?appid=" + Config.getAppid() + "&appkey=" + Config.getAppKey() + "&code=" + code)
                .get();
        return Token.fromJsonObject(request.execute().getJSONObject());
    }

//刷新access_token
    public static Token refreshAccessToken(String refreshToken) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("appkey", Config.getAppKey());
        jsonBuilder.put("refresh_token", refreshToken);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/token/refresh")
                .post(new RequestBody(jsonBuilder));
        return Token.fromJsonObject(request.execute().getJSONObject());
    }

//拉取用户信息
    public static UserInfo getUserInfo(String access_token, String openid) throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/user?appid=" + Config.getAppid() + "&access_token=" + access_token + "&openid=" + openid)
                .get();
        return UserInfo.fromJsonObject(request.execute().getJSONObject());
    }

//拉取用户信息(建议用新版)
    public static UserInfo getUserInfoV3(String access_token, String openid) throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/user?appid=" + Config.getAppid() + "&access_token=" + access_token + "&openid=" + openid)
                .get();
        return UserInfo.fromJsonObject(request.execute().getJSONObject());
    }

//获取rpc_token
    public static String getRPCToken(String accessToken, String scope) throws YunException {
            JSONBuilder jsonBuilder = new JSONBuilder();
            jsonBuilder.put("access_token", accessToken);
            jsonBuilder.put("scope", scope);
            jsonBuilder.put("appid", Config.getAppid());
            Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/rpc/token")
                    .post(new RequestBody(jsonBuilder));
            return request.execute().getJSONObject().optString("rpc_token");
    }

    public static AppToken getAppToken() throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/app/token?appid=" + Config.getAppid() + "&appkey=" + Config.getAppKey())
                .get();
        return AppToken.fromJsonObject(request.execute().getJSONObject());
    }
}
