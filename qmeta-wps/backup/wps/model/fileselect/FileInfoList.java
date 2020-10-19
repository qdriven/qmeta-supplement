package cn.wps.model.fileselect;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class FileInfoList {
    public long result;
    public List<SelectFileInfo> fileInfos;

    public static FileInfoList fromJsonObject(JSONObject jsonObject) {
        FileInfoList fileList = new FileInfoList();

        fileList.result = jsonObject.optLong("result");
        JSONArray fileArray = jsonObject.optJSONArray("file_info_list");
        if (fileArray != null) {
            fileList.fileInfos = new ArrayList<SelectFileInfo>();
            for (int i = 0, len = fileArray.length(); i < len; i++) {
                JSONObject fileInfoObject = (JSONObject)fileArray.opt(i);
                SelectFileInfo fileInfo = new SelectFileInfo();
                fileInfo.fileid = fileInfoObject.optString("open_file_id");
                fileInfo.fname = fileInfoObject.optString("fname");
                fileInfo.fsize = fileInfoObject.optLong("fsize");
                fileInfo.mtime = fileInfoObject.optLong("mtime");
                fileList.fileInfos.add(fileInfo);
            }
        }
        return fileList;
    }
}
