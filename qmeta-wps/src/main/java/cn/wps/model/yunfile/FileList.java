package cn.wps.model.yunfile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FileList {
    public long result;
    public List<FileInfo> fileInfos;

    public static FileList fromJsonObject(JSONObject jsonObject) {
        FileList fileList = new FileList();

        fileList.result = jsonObject.optLong("result");
        JSONArray fileArray = jsonObject.optJSONArray("files");
        if (fileArray != null) {
            fileList.fileInfos = new ArrayList<FileInfo>();
            for (int i = 0, len = fileArray.length(); i < len; i++) {
                JSONObject fileInfoObject = (JSONObject)fileArray.opt(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.fname = fileInfoObject.optString("file_name");
                fileInfo.fileid = fileInfoObject.optString("open_fileid");
                fileInfo.ftype = fileInfoObject.optString("file_type");
                fileInfo.fsize = fileInfoObject.optLong("file_size");
                fileInfo.ctime = fileInfoObject.optLong("ctime");
                fileInfo.mtime = fileInfoObject.optLong("mtime");
                fileList.fileInfos.add(fileInfo);
            }
        }
        return fileList;
    }
}
