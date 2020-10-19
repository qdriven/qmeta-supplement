package cn.wps.model.yunfile;

import org.json.JSONObject;

public class LinkInfo {
    public long result;
    public String linkurl;
    public String fname;
    public String ftype;
    public long fsize;
    public String permission;
    public String status;
    public long expireTime;


    public static LinkInfo fromJsonObject(JSONObject jsonObject)
    {
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.result = jsonObject.optLong("result");
        linkInfo.linkurl = jsonObject.optString("link_url");
        linkInfo.fname = jsonObject.optString("file_name");

        return  linkInfo;
    }

    public static LinkInfo fromJsonObject1(JSONObject jsonObject)
    {
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.result = jsonObject.optLong("result");
        JSONObject linkInfoObject = jsonObject.optJSONObject("link_info");
        if (linkInfoObject == null)
            return linkInfo;
        linkInfo.fname = linkInfoObject.optString("file_name");
        linkInfo.ftype = linkInfoObject.optString("file_type");
        linkInfo.fsize = linkInfoObject.optLong("file_size");
        linkInfo.permission = linkInfoObject.optString("link_permission");
        linkInfo.status = linkInfoObject.optString("status");
        linkInfo.expireTime = linkInfoObject.optLong("expire_time");

        return  linkInfo;
    }
}
