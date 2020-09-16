package cn.wps.openapi;

import cn.wps.exception.YunException;
import cn.wps.http.JSONBuilder;
import cn.wps.http.Request;
import cn.wps.http.RequestBody;
import cn.wps.model.*;
import cn.wps.model.yunfile.*;
import cn.wps.util.FileUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

public class YunFile {

    //获取APP剩余空间
    public static RemainingSpace getRemainingSpace(String accessToken) throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/appfile/remaining?appid=" + Config.getAppid() + "&access_token=" + accessToken)
                .get();
        return RemainingSpace.fromJsonObject(request.execute().getJSONObject());
    }

    //获取APP文件列表 （可选参数不需要传null即可）
    public static FileList getAppFileList(String accessToken, String parentid, String order, String orderby, String offset, String count) throws YunException {
        String requestUrl = Config.getOpenApiServer() + "/oauthapi/v3/app/files/list?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&open_parentid=" + parentid;
        if(order != null) {
            requestUrl += "&order=" + order;
        }
        if(orderby != null) {
            requestUrl += "&order_by=" + orderby;
        }
        if(offset != null) {
            requestUrl += "&offset=" + offset;
        }
        if(count != null) {
            requestUrl += "&count=" + count;
        }

        Request request = new Request().url(requestUrl)
                .get();
        return FileList.fromJsonObject(request.execute().getJSONObject());
    }

    //创建文件夹
    public static CreateFolder createFolder(String accessToken, String parentid, String name) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_parentid", parentid);
        jsonBuilder.put("name", name);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/folders/create")
                .post(new RequestBody(jsonBuilder));
        return CreateFolder.fromJsonObject(request.execute().getJSONObject());
    }

    //上传文件
    public static FileInfo uploadFile(String accessToken, String parentid, File file, Boolean addNameIndex) throws YunException, JSONException {
        String sha1 = FileUtil.getFileSHA1(file);
        UploadUrlData uploadUrlData = getUploadUrl(accessToken, parentid, file.length(), file.getName(), sha1);
        if (uploadUrlData != null) {
            UploadCommitData uploadCommitData = upload2CDN(uploadUrlData, file);
            return uploadFileInfo(accessToken, parentid, file.length(), sha1, file.getName(), uploadCommitData.etag, addNameIndex);
        }
        return null;
    }


    //更新文件
    public static FileInfo updateFile(String accessToken, String parentid, String fileid, File file) throws YunException, JSONException {
        String sha1 = FileUtil.getFileSHA1(file);
        UploadUrlData uploadUrlData = getUploadUrl(accessToken, parentid, file.length(), file.getName(), sha1);
        if (uploadUrlData != null) {
            UploadCommitData uploadCommitData = upload2CDN(uploadUrlData, file);
            return updateFileInfo(accessToken, parentid, fileid, file.length(), sha1, file.getName(), uploadCommitData.etag);
        }
        return null;
    }

    //获取分享文件链接
    public static LinkInfo getFileLinkInfo(String accessToken, String fileid, String permission, String period) throws YunException {
        String requestUrl = Config.getOpenApiServer() + "/oauthapi/v3/app/files/link?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&open_fileid=" + fileid;
        if(permission != null) {
            requestUrl += "&permission=" + permission;
        }
        if(period != null) {
            requestUrl += "&period=" + period;
        }
        Request request = new Request().url(requestUrl)
                .get();
        return LinkInfo.fromJsonObject(request.execute().getJSONObject());
    }

    //获取文件下载链接
    public static FileDownloadUrl getFileDownloadUrl(String accessToken, String fileid) throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/files/download/url?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&open_fileid=" + fileid)
                .get();
        return FileDownloadUrl.fromJsonObject(request.execute().getJSONObject());
    }

    //文件重命名
    public static CommonResult fileRename(String accessToken, String fileid, String newname) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_fileid", fileid);
        jsonBuilder.put("new_name", newname);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/files/rename")
                .put(new RequestBody(jsonBuilder));
        return CommonResult.fromJsonObject(request.execute().getJSONObject());
    }

    //文件复制
    public static CommonResult fileCopyInApp(String accessToken, String fileids, String fromParentid, String toParentid) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_fileids", fileids);
        jsonBuilder.put("open_from_parentid", fromParentid);
        jsonBuilder.put("open_to_parentid", toParentid);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/files/copy")
                .post(new RequestBody(jsonBuilder));
        return CommonResult.fromJsonObject(request.execute().getJSONObject());
    }

    //文件移动
    public static CommonResult fileMoveInApp(String accessToken, String fileids, String fromParentid, String toParentid) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_fileids", fileids);
        jsonBuilder.put("open_from_parentid", fromParentid);
        jsonBuilder.put("open_to_parentid", toParentid);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/files/move")
                .post(new RequestBody(jsonBuilder));
        return CommonResult.fromJsonObject(request.execute().getJSONObject());
    }

    //文件删除
    public static CommonResult fileDelete(String accessToken, String fileids) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_fileids", fileids);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/files/delete")
                .delete(new RequestBody(jsonBuilder));
        return CommonResult.fromJsonObject(request.execute().getJSONObject());
    }

    //文件搜索 (按文件名)
    public static FileList serchByName(String accessToken, String parentid, String fname, String offset, String count) throws YunException {
        String requestUrl = Config.getOpenApiServer() + "/oauthapi/v3/app/files/searchbyname?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&file_name=" + fname + "&open_parentid=" + parentid;
        if(offset != null) {
            requestUrl += "&offset=" + offset;
        }
        if(count != null) {
            requestUrl += "&count=" + count;
        }

        Request request = new Request().url(requestUrl)
                .get();
        return FileList.fromJsonObject(request.execute().getJSONObject());
    }

    //文件搜索 (按内容)
    public static FileList serchByContent(String accessToken, String parentid, String content, String offset, String count) throws YunException {
        String requestUrl = Config.getOpenApiServer() + "/oauthapi/v3/app/files/searchbycontent?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&content=" + content + "&open_parentid=" + parentid;
        if(offset != null) {
            requestUrl += "&offset=" + offset;
        }
        if(count != null) {
            requestUrl += "&count=" + count;
        }

        Request request = new Request().url(requestUrl)
                .get();
        return FileList.fromJsonObject(request.execute().getJSONObject());
    }

    //新建文件
    public static CreateFile createFile(String accessToken, String parentid, String name, Boolean addNameIndex) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_parentid", parentid);
        jsonBuilder.put("file_name", name);
        jsonBuilder.put("add_name_index", addNameIndex);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/files/create")
                .post(new RequestBody(jsonBuilder));
        return CreateFile.fromJsonObject(request.execute().getJSONObject());
    }

    //获取文件信息
    public static FileInfo getFileInfo(String accessToken, String fileid) throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/file/info?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&open_fileid=" + fileid)
                .get();
        return FileInfo.fromJsonObject1(request.execute().getJSONObject());
    }

    //获取分享链接信息
    public static LinkInfo getLinkInfo(String accessToken, String linkUrl) throws YunException {
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/file/link/info?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&link_url=" + linkUrl)
                .get();
        return LinkInfo.fromJsonObject1(request.execute().getJSONObject());
    }

    private static UploadUrlData getUploadUrl(String accessToken, String parentid, long size, String name, String sha1) throws YunException, JSONException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_parentid", parentid);
        jsonBuilder.put("size", size);
        jsonBuilder.put("name", name);
        jsonBuilder.put("sha1", sha1);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v4/app/files/upload/request")
                .put(new RequestBody(jsonBuilder));

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

    private static FileInfo uploadFileInfo(String accessToken, String parentid, long size, String sha1, String name, String etag, Boolean addNameIndex) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_parentid", parentid);
        jsonBuilder.put("size", size);
        jsonBuilder.put("sha1", sha1);
        jsonBuilder.put("name", name);
        jsonBuilder.put("etag", etag);
        jsonBuilder.put("add_name_index", addNameIndex);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/files/upload/commit")
                .post(new RequestBody(jsonBuilder));
        return FileInfo.fromJsonObject(request.execute().getJSONObject());
    }

    private static FileInfo updateFileInfo(String accessToken, String parentid, String fileid, long size, String sha1, String name, String etag) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("open_parentid", parentid);
        jsonBuilder.put("open_fileid", fileid);
        jsonBuilder.put("size", size);
        jsonBuilder.put("sha1", sha1);
        jsonBuilder.put("name", name);
        jsonBuilder.put("etag", etag);
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v3/app/files/upload/update")
                .post(new RequestBody(jsonBuilder));
        return FileInfo.fromJsonObject(request.execute().getJSONObject());
    }
}
