package cn.wps.openapi;

public class Config {
    private static String Appid = null;
    private static String AppKey = null;
    private static String OpenApiServer = "https://openapi.wps.cn";

    public static void setConfig(String appid, String appKey) {
        Config.Appid = appid;
        Config.AppKey = appKey;
    }

    public static void setConfig(String appid, String appKey, String openApiServer) {
        Config.Appid = appid;
        Config.AppKey = appKey;
        Config.OpenApiServer = openApiServer;
    }

    public static String getAppid() {
        return Appid;
    }

    public static void setAppid(String appid) {
        Appid = appid;
    }

    public static String getAppKey() {
        return AppKey;
    }

    public static void setAppKey(String appKey) {
        AppKey = appKey;
    }

    public static String getOpenApiServer() {
        return OpenApiServer;
    }

    public static void setOpenApiServer(String openApiServer) {
        OpenApiServer = openApiServer;
    }
}
