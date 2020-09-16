package cn.wps.model.yunfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UploadUrlData {
    public String upload_url;
    public Map<String, String> headers;

    public static UploadUrlData fromJsonObject(JSONObject jsonObject) throws JSONException
    {
        JSONObject data = jsonObject.optJSONObject("data");
        if (data == null)
            return null;
        JSONObject uploadInfo = data.optJSONObject("uploadinfo");
        if (uploadInfo == null)
            return null;
        JSONObject putAuth = uploadInfo.optJSONObject("put_auth");
        if (putAuth == null)
            return null;
        JSONObject headers = uploadInfo.optJSONObject("headers");
        if (headers == null)
            return null;

        UploadUrlData uploadUrlData = new UploadUrlData();
        uploadUrlData.upload_url = putAuth.optString("upload_url");
        uploadUrlData.headers = new HashMap<String, String>();

        Iterator it = headers.keys();
        while(it.hasNext()){
            String key = (String) it.next();
            String value = headers.getString(key);
            uploadUrlData.headers.put(key, value);
        }
        return uploadUrlData;
    }
}
