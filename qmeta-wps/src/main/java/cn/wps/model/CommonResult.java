package cn.wps.model;

import org.json.JSONObject;

public class CommonResult {
    public String msg;
    public long result;

    public static CommonResult fromJsonObject(JSONObject jsonObject)
    {
        CommonResult fileRename = new CommonResult();
        fileRename.msg = jsonObject.optString("msg");
        fileRename.result = jsonObject.optLong("result");
        return fileRename;
    }
}
