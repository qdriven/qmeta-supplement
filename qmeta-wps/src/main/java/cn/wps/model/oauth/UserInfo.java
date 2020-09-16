package cn.wps.model.oauth;

import org.json.JSONObject;

public class UserInfo {
    public String nickname;
    public String avatar;
    public String sex;
    public String openid;
    public String unionid;

    public static UserInfo fromJsonObject(JSONObject jsonObject)
    {
        JSONObject userInfoObject = jsonObject.optJSONObject("user");
        if (userInfoObject == null)
            return null;
        UserInfo userInfo = new UserInfo();
        userInfo.nickname = userInfoObject.optString("nickname");
        userInfo.avatar = userInfoObject.optString("avatar");
        userInfo.sex = userInfoObject.optString("sex");
        userInfo.openid = userInfoObject.optString("openid");
        userInfo.unionid = userInfoObject.optString("unionid");
        return userInfo;
    }
}
