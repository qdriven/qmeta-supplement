package cn.wps.http;

import cn.wps.exception.YunHttpIOException;
import okhttp3.OkHttpClient;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * cn.wps.http 是对okhttp或HttpClient 的简单封装，目的是让上层和网络层解耦
 *
 * 同时提供一批工具类，比如：
 *
 * 1.JSON 构造器
 * 2.URL 构造器
 *
 *
 */
public final class Request {
    private static OkHttpClient client = createClient();
    private okhttp3.Request.Builder builder = new okhttp3.Request.Builder();

    private static void resetClient() {
        client.connectionPool().evictAll();
        client = createClient();
    }

    private static OkHttpClient createClient() {
        //防止SSLPeerUnverifiedException问题，加verifier
        OkHttpClient okHttpClient =  new OkHttpClient.Builder()
                .hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();
        return okHttpClient;
    }

    public Request() {

    }

    public Request url(String url) {
        builder.url(url);
        return this;
    }

    public Request addHeader(String name, String value) {
        builder.addHeader(name, value);
        return this;
    }

    public Request addHeader(String name, boolean value) {
        builder.addHeader(name, Boolean.toString(value));
        return this;
    }

    public Request get() {
        builder.get();
        return this;
    }

    public Request post(RequestBody body) {
        builder.post(body == null ? new RequestBody().create() : body.create());
        return this;
    }

    public Request put(RequestBody body) {
        builder.put(body == null ? new RequestBody().create() : body.create());
        return this;
    }

    public Request delete() {
        builder.delete();
        return this;
    }

    public Request delete(RequestBody body) {
        builder.delete(body.create());
        return this;
    }

    public static void setProxy(String hostname, int port) {
        InetSocketAddress address = new InetSocketAddress(hostname, port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        client.newBuilder().proxy(proxy);
    }

    public static void cleanProxy() {
        client.newBuilder().proxy(Proxy.NO_PROXY);
    }


    /**
     * 问题：用户访问某个域名，可能会持续出现java.net.SocketTimeoutException
     *
     * 猜测：可能是okhttp缓存的原因
     *
     * 解决方案：重置okhttpclient
     *
     * @return
     * @throws IOException
     */
    public Response execute() throws YunHttpIOException {
        okhttp3.Request request = builder.build();
        try {
            Response response = new Response(client.newCall(request).execute());
            return response;
        } catch (IOException e) {
            Request.resetClient();
            try {
                Response response = new Response(client.newCall(request).execute());
                return response;
            } catch (IOException e1) {
                throw new YunHttpIOException(e);
            }
        }
    }
}
