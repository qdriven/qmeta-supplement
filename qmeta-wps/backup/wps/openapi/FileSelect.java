package cn.wps.openapi;

import cn.wps.exceptio.YunException;
import cn.wps.http.JSONBuilder;
import cn.wps.http.Request;
import cn.wps.http.RequestBody;
import cn.wps.model.*;
import cn.wps.model.fileselect.*;
import org.json.JSONException;

import java.io.File;

public class FileSelect {

    //获取文件信息列表
    public static FileInfoList getFileInfoList(String accessToken, String fileCode) throws YunException {
        String requestUrl = Config.getOpenApiServer() + "/oauthapi/v2/selector/file/info?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&file_code=" + fileCode;
        Request request = new Request().url(requestUrl)
                .get();
        return FileInfoList.fromJsonObject(request.execute().getJSONObject());
    }

    //获取文件分享链接列表
    public static ShareFileList getShareFileList(String accessToken, String fileCode, String ranges, String permission, String period) throws YunException {
        String requestUrl = Config.getOpenApiServer() + "/oauthapi/v2/selector/share/info?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&file_code=" + fileCode;
        if(ranges != null) {
            requestUrl += "&ranges=" + ranges;
        }
        if(permission != null) {
            requestUrl += "&permission=" + permission;
        }
        if(period != null) {
            requestUrl += "&period=" + period;
        }
        Request request = new Request().url(requestUrl)
                .get();
        return ShareFileList.fromJsonObject(request.execute().getJSONObject());
    }

    //获取文件下载链接列表
    public static DownloadFileList getDownloadFileList(String accessToken, String fileCode) throws YunException {
        String requestUrl = Config.getOpenApiServer() + "/oauthapi/v2/selector/download/url?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&file_code=" + fileCode;
        Request request = new Request().url(requestUrl)
                .get();
        return DownloadFileList.fromJsonObject(request.execute().getJSONObject());
    }

    //上传文件
    public static UploadFileInfo uploadFile(String accessToken, String fileCode, File file, Boolean addNameIndex) throws YunException, JSONException {
        UploadUrlData uploadUrlData = getUploadUrl(accessToken, fileCode, file.length(), file.getName());
        if (uploadUrlData != null) {
            UploadCommitData uploadCommitData = upload2CDN(uploadUrlData, file);
            return uploadFileInfo(accessToken, fileCode, file.length(), uploadCommitData.fsha1, file.getName(), uploadCommitData.etag, addNameIndex);
        }
        return null;
    }

    private static UploadUrlData getUploadUrl(String accessToken, String fileCode, long size, String name) throws YunException, JSONException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/selector/upload/info?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&file_code=" + fileCode + "&size=" + String.valueOf(size) + "&name=" + name)
                .get();
        return UploadUrlData.fromJsonObject(request.execute().getJSONObject());
    }

    private static UploadCommitData upload2CDN(UploadUrlData uploadUrlData, File file) throws YunException {
        RequestBody requestBody = new RequestBody(file);

        Request request = new Request().url(uploadUrlData.upload_url)
                .put(requestBody);
        for (String key : uploadUrlData.headers.keySet()) {
            request.addHeader(key, uploadUrlData.headers.get(key));
        }
        return UploadCommitData.fromReponse(request.execute());
    }

    private static UploadFileInfo uploadFileInfo(String accessToken, String fileCode, long size, String sha1, String name, String etag, Boolean addNameIndex) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("file_code", fileCode);
        jsonBuilder.put("size", size);
        jsonBuilder.put("sha1", sha1);
        jsonBuilder.put("name", name);
        jsonBuilder.put("etag", etag);
        jsonBuilder.put("add_name_index", addNameIndex);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/selector/file/create")
                .post(new RequestBody(jsonBuilder));
        return UploadFileInfo.fromJsonObject(request.execute().getJSONObject());
    }

}
