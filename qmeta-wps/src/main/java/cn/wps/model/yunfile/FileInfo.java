package cn.wps.model.yunfile;

import org.json.JSONObject;

public class FileInfo {
    public String fname;
    public String fileid;
    public String ftype;
    public long fsize;
    public long ctime;
    public long mtime;

    public static FileInfo fromJsonObject(JSONObject jsonObject) {
        JSONObject fileInfoObject = jsonObject.optJSONObject("data");
        if (fileInfoObject == null)
            return null;
        FileInfo fileInfo = new FileInfo();
        fileInfo.fname = fileInfoObject.optString("file_name");
        fileInfo.fileid = fileInfoObject.optString("open_fileid");
        fileInfo.ftype = fileInfoObject.optString("file_type");
        fileInfo.fsize = fileInfoObject.optLong("file_size");
        fileInfo.ctime = fileInfoObject.optLong("ctime");
        fileInfo.mtime = fileInfoObject.optLong("mtime");
        return fileInfo;
    }

    public static FileInfo fromJsonObject1(JSONObject jsonObject) {
        JSONObject fileInfoObject = jsonObject.optJSONObject("file_info");
        if (fileInfoObject == null)
            return null;
        FileInfo fileInfo = new FileInfo();
        fileInfo.fname = fileInfoObject.optString("file_name");
        fileInfo.ftype = fileInfoObject.optString("file_type");
        fileInfo.fsize = fileInfoObject.optLong("file_size");
        fileInfo.ctime = fileInfoObject.optLong("ctime");
        fileInfo.mtime = fileInfoObject.optLong("mtime");
        return fileInfo;
    }
}
