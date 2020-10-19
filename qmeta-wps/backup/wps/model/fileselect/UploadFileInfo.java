package cn.wps.model.fileselect;

import org.json.JSONObject;

public class UploadFileInfo {
    public String fileid;
    public String fname;
    public long fsize;

    public static UploadFileInfo fromJsonObject(JSONObject jsonObject) {
        JSONObject fileInfoObject = jsonObject.optJSONObject("file_info");
        if (fileInfoObject == null)
            return null;
        UploadFileInfo fileInfo = new UploadFileInfo();
        fileInfo.fileid = fileInfoObject.optString("open_file_id");
        fileInfo.fname = fileInfoObject.optString("fname");
        fileInfo.fsize = fileInfoObject.optLong("fsize");
        return fileInfo;
    }
}
