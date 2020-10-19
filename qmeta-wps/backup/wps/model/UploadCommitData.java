package cn.wps.model;

import cn.wps.exception.YunException;
import cn.wps.http.Response;

public class UploadCommitData {
    public String fsha1;
    public String etag;

    public static UploadCommitData fromReponse(Response response) throws YunException {
        UploadCommitData uploadCommitData = new UploadCommitData();

        uploadCommitData.fsha1 = response.getJSONObject().optString("newfilename");
        uploadCommitData.etag = response.getHeader("etag").replaceAll("\"", "");

        return uploadCommitData;
    }
}
