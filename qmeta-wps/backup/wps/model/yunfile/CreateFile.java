package cn.wps.model.yunfile;

import org.json.JSONObject;

public class CreateFile {
    public String fileid;
    public String fname;
    public long result;

    public static CreateFile fromJsonObject(JSONObject jsonObject) {
        CreateFile createFile = new CreateFile();
        createFile.result = jsonObject.optLong("result");

        JSONObject fileInfoObject = jsonObject.optJSONObject("file");
        if (fileInfoObject == null)
            return  createFile;
        createFile.fileid = fileInfoObject.optString("open_fileid");
        createFile.fname = fileInfoObject.optString("file_name");

        return  createFile;
    }
}

