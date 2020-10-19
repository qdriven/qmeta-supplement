package cn.wps.model.fileselect;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ShareFileList {
    public long result;
    public List<ShareFileInfo> fileInfos;

    public static ShareFileList fromJsonObject(JSONObject jsonObject) {
        ShareFileList fileList = new ShareFileList();

        fileList.result = jsonObject.optLong("result");
        JSONArray fileArray = jsonObject.optJSONArray("share_info_list");
        if (fileArray != null) {
            fileList.fileInfos = new ArrayList<ShareFileInfo>();
            for (int i = 0, len = fileArray.length(); i < len; i++) {
                JSONObject fileInfoObject = (JSONObject)fileArray.opt(i);
                ShareFileInfo fileInfo = new ShareFileInfo();
                fileInfo.fileid = fileInfoObject.optString("open_file_id");
                fileInfo.fname = fileInfoObject.optString("fname");
                fileInfo.url = fileInfoObject.optString("url");
                fileInfo.ranges = fileInfoObject.optString("ranges");
                fileInfo.permission = fileInfoObject.optString("permission");
                fileInfo.expirePeriod = fileInfoObject.optLong("expire_period");
                fileInfo.status = fileInfoObject.optString("status");
                fileList.fileInfos.add(fileInfo);
            }
        }
        return fileList;
    }
}
