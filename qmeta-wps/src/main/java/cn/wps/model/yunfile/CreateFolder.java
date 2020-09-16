package cn.wps.model.yunfile;

import org.json.JSONObject;

public class CreateFolder {
    public String fname;
    public String folderid;
    public long result;

    public static CreateFolder fromJsonObject(JSONObject jsonObject) {
        CreateFolder createFolder = new CreateFolder();
        createFolder.result = jsonObject.optLong("result");

        JSONObject folderInfoObject = jsonObject.optJSONObject("data");
        if (folderInfoObject == null)
            return createFolder;
        createFolder.fname = folderInfoObject.optString("file_name");
        createFolder.folderid = folderInfoObject.optString("open_folderid");

        return createFolder;
    }
}
