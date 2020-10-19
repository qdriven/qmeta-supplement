package cn.wps.model.yunfile;

import org.json.JSONObject;

public class FileDownloadUrl {
    public long result;
    public String url;

    public static FileDownloadUrl fromJsonObject(JSONObject jsonObject)
    {
        FileDownloadUrl fileDownloadUrl = new FileDownloadUrl();
        fileDownloadUrl.result = jsonObject.optLong("result");
        fileDownloadUrl.url = jsonObject.optString("url");
        return  fileDownloadUrl;
    }
}
