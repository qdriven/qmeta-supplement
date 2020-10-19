package cn.wps.model.fileselect;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DownloadFileList {
    public long result;
    public List<SelectFileInfo> file_infos;

    public static DownloadFileList fromJsonObject(JSONObject jsonObject) {
        DownloadFileList fileList = new DownloadFileList();

        fileList.result = jsonObject.optLong("result");
        JSONArray fileArray = jsonObject.optJSONArray("download_info_list");
        if (fileArray != null) {
            fileList.file_infos = new ArrayList<SelectFileInfo>();
            for (int i = 0, len = fileArray.length(); i < len; i++) {
                JSONObject fileInfoObject = (JSONObject)fileArray.opt(i);
                SelectFileInfo fileInfo = new SelectFileInfo();
                fileInfo.fileid = fileInfoObject.optString("open_file_id");
                fileInfo.url = fileInfoObject.optString("url");
                fileList.file_infos.add(fileInfo);
            }
        }
        return fileList;
    }
}
