package io.qmeta.testng.util;

import io.ift.automation.testscaffold.apitest.ModifiedFastJsonHttpMessageConverter;
import io.ift.automation.testscaffold.apitest.ModifiedResponseErrorHandler;
import io.ift.automation.testscaffold.apitest.ModifiedTextJsonHttpMessageConverter;
import com.google.common.collect.Lists;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * 封装过的RestTemplateClient 基于Spring Rest Template
 * @param <T> HttpEntity
 */

@SuppressWarnings("unchecked")
public final class RestTemplateClientHelper<T> {
    private static final Logger logger = LogManager.getLogger(RestTemplateClientHelper.class.getName());
    private RestTemplate template;
    private HttpHeaders headers;
    private List messageConverters;
    private HttpEntity<T> httpEntity;
    private T body;

    private static final Charset UTF_CHARSET=Charset.forName("UTF-8");
    private static final List<MediaType> UTF8s = Lists.newArrayList(
            new MediaType("application","json",UTF_CHARSET)
            ,new MediaType("application","xml",UTF_CHARSET)
            ,new MediaType("text","html",UTF_CHARSET)
            ,new MediaType("text","plain",UTF_CHARSET)
            ,new MediaType("multipart","form-data",UTF_CHARSET)
            ,new MediaType("application","x-www-form-urlencoded",UTF_CHARSET)
    );
    private static final List<MediaType> StringUTF8 = Lists.newArrayList(
            new MediaType("text","html",UTF_CHARSET)
            ,new MediaType("text","plain",UTF_CHARSET)
            ,new MediaType("multipart","form-data",UTF_CHARSET)
            ,new MediaType("application","x-www-form-urlencoded",UTF_CHARSET)
    );

    private RestTemplateClientHelper(){}

    /**
     * 获取RestTemplateClient 实例,实际使用可以基本考虑不使用
     * @return return a new RestTemplateClient
     */
    @Deprecated
    public static RestTemplateClientHelper getInstance(){
        RestTemplateClientHelper client = new RestTemplateClientHelper();
        client.setTemplate(new RestTemplate());
        client.getTemplate().setErrorHandler(new ModifiedResponseErrorHandler());
        return new RestTemplateClientHelper();
    }

    /**
     * 获取默认的RestTemplate
     * @return return a new RestTemplateClient
     */
    @Deprecated
    public static RestTemplateClientHelper getDefaultInstance(){
        RestTemplateClientHelper client = new RestTemplateClientHelper();
        client.setTemplate(new RestTemplate());
        client.getTemplate().setErrorHandler(new ModifiedResponseErrorHandler());
        client.useDefaultHeaders().useDefaultMessageConverter();
        return client;
    }

    /**
     * 获取HTTPClient实现的Rest template
     * @return return a new RestTemplateClient
     */
    @Deprecated
    public static RestTemplateClientHelper getHttpClientImplInstance(){
        return defaultInstance();
    }

    /**
     * default http service client
     * @return
     */
    public static RestTemplateClientHelper defaultInstance(){
        RestTemplateClientHelper client = new RestTemplateClientHelper();
        HttpClient httpClient = getIgnoreSSLHttpClient();
        client.setTemplate(new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient)));
        client.getTemplate().setErrorHandler(new ModifiedResponseErrorHandler());
        client.useDefaultHeaders().useDefaultMessageConverter();
        return client;
    }

    /**
     * 获取忽略SSL的httpclient，支持https的请求
     * @return
     */
    private static HttpClient getIgnoreSSLHttpClient() {
        CloseableHttpClient httpClient = null;
        try {

            httpClient = HttpClients.custom().
                    setHostnameVerifier(new AllowAllHostnameVerifier()).
                    setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                            return true;
                        }
                    }).build()).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            logger.error(e);
        }
        return httpClient;
    }

    /**
     * 获取HTTPClient实现的Rest template
     * @return return a new RestTemplateClient
     */
    public static RestTemplateClientHelper getInstanceWODefaultConfiguration(){
        RestTemplateClientHelper client = new RestTemplateClientHelper();
        client.setTemplate(new RestTemplate(new HttpComponentsClientHttpRequestFactory()));
        return client;
    }
    /**
     * remove message converter
     * @return rest template client
     */
    @SuppressWarnings("unchecked")
    public RestTemplateClientHelper removeMessageConverter(){
        messageConverters = null;
        return this;
    }

    /**
     * 初始化默认的MessageConverter
     * @return
     */
    @SuppressWarnings("unchecked")
    public RestTemplateClientHelper useDefaultMessageConverter(){
        if(messageConverters==null) messageConverters =new ArrayList();
        messageConverters.add(new SourceHttpMessageConverter());
        FormHttpMessageConverter converter = new FormHttpMessageConverter();
        converter.setSupportedMediaTypes(UTF8s);
        messageConverters.add(converter);
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setSupportedMediaTypes(StringUTF8);
        messageConverters.add(stringConverter);
        messageConverters.add(new ModifiedFastJsonHttpMessageConverter());
        messageConverters.add(new ModifiedTextJsonHttpMessageConverter());
//        messageConverters.add(new MappingJackson2HttpMessageConverter());
        AllEncompassingFormHttpMessageConverter encompassingConvert = new AllEncompassingFormHttpMessageConverter();
        encompassingConvert.setSupportedMediaTypes(UTF8s);
        messageConverters.add(encompassingConvert);
        template.setMessageConverters(messageConverters);

        return this;
    }

    /**
     * add customer header
     * 添加非默认的http头
     * @param key header description
     * @param value header value
     * @return RestTemplateClient
     */
    public RestTemplateClientHelper addHeader(String key,String value){
        if(headers==null) headers = new HttpHeaders();
        headers.add(key,value);
        return this;
    }

    public RestTemplateClientHelper restAndAddHeader(String key,String value){
        headers = new HttpHeaders();
        headers.add(key,value);
        return this;
    }

    /**
     * add customer header
     * 添加非默认的http头
     * @param key header description
     * @param value header value
     * @return RestTemplateClient
     */
    public RestTemplateClientHelper addHeader(String key,List<String> value){
        if(headers==null) headers = new HttpHeaders();
        headers.put(key, value);
        return this;
    }

    /**
     * 生成request
     * @return RestTemplateClient
     */
    public RestTemplateClientHelper buildRequest(){
        if(body==null){
            httpEntity = new HttpEntity(headers);
        }else{
            httpEntity = new HttpEntity<T>(body,headers);
        }

        return this;
    }

    /**
     * 增加请求的body
     * @param body
     * @return RestTemplateClient
     */
    public RestTemplateClientHelper addBody(T body){
        this.body =body;
        return this;
    }

    /**
     * 添加认证信息到请求头(Header)中
     * @param value
     * @return
     */
    public RestTemplateClientHelper addAuthHeader(String value){
        this.addHeader("Authorization", value);
        return this;
    }

    public RestTemplateClientHelper addBearerToken(String value){
        this.addHeader("Authorization", "Bearer "+value);
        return this;
    }

    /**
     * 增加X-TOKEN 值
     * @param value
     * @return
     */
    public RestTemplateClientHelper addXToken(String value){
        this.addHeader("X-Token",value);
        return this;
    }

    /**
     * 删除X-TOKEN值
     * @return
     */
    public RestTemplateClientHelper removeXToken(){
        this.getHeaders().remove("X-Token");
        return this;
    }

    /**
     * 替换X-TOKEN值
     * @param value
     * @return
     */
    public RestTemplateClientHelper replaceXToken(String value){
        this.getHeaders().remove("X-Token");
        this.getHeaders().add("X-Token",value);
        return this;
    }

    /**
     * 使用默认的请求头
     * @return RestTemplateClient
     */
    public RestTemplateClientHelper useDefaultHeaders(){
        if(headers==null) headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json",UTF_CHARSET));
//        headers.setAccept(Lists.newArrayList(MediaType.APPLICATION_JSON,
//                MediaType.APPLICATION_XML, MediaType.TEXT_HTML,MediaType.TEXT_PLAIN,MediaType.ALL));
        headers.setAccept(UTF8s);
        headers.setAcceptCharset(Lists.newArrayList(Charset.forName("utf-8")));
        headers.add("Accept-Language","zh-CN,zh;q=0.8");
        headers.add("Content-Encoding", "UTF-8");
        return this;
    }


    public void removeAuthHeader(){
        if (headers!=null) headers.remove("Authorization");
    }

    /**
     * 发送请求
     * @param requestUrl
     * @param method
     * @return request's response
     */
    public ResponseEntity<String> call(String requestUrl,HttpMethod method){
        this.body=null;
        buildRequest();
        logger.info("headEntity={}",httpEntity);
        return template.exchange(requestUrl,method,httpEntity,String.class);
    }

    /**
     * 发送请求，返回的body是给定的类，已经将JSON转换成java Bean了
     * @param requestUrl
     * @param method
     * @param rClazz
     * @return
     */
    public ResponseEntity<T> call(String requestUrl,HttpMethod method,Class<T> rClazz){
        this.body=null;
        buildRequest();
        logger.info("header={}",httpEntity);
        return template.exchange(requestUrl,method,httpEntity,rClazz);
    }

    /**
     * 发送带request body的请求
     * @param requestUrl
     * @param method
     * @return request's response
     */
    public ResponseEntity<String> call(String requestUrl,HttpMethod method,T body){
        addBody(body);
        buildRequest();
        logger.info("httpEntity={}", httpEntity);
        logger.info("requestUrl={}",requestUrl);
        return template.exchange(requestUrl,method,httpEntity,String.class);
    }

    public ResponseEntity<String> post(String requestUrl,T body){
        try {
            RequestEntity request = new RequestEntity(body,this.headers,HttpMethod.POST,new URI(requestUrl));
            logger.info(request.toString());
            return template.exchange(request,String.class);
        } catch (URISyntaxException e) {
            logger.error("URL is not correct");
        }
        throw new RuntimeException("URL is not correct");
    }
    /**
     * 发送请求，返回的body是给定的类，已经将JSON转换成java Bean了
     * @param requestUrl
     * @param method
     * @param body
     * @param tClass
     * @return
     */
    public ResponseEntity<T> call(String requestUrl,HttpMethod method,T body,Class<T> tClass){
        addBody(body);
        buildRequest();
        logger.info("httpEntity={}",httpEntity);
        logger.info("requestUrl={}",requestUrl);
        return template.exchange(requestUrl,method,httpEntity,tClass);
    }


    /**
     * 设置请求的content type
     * @param mediaType
     * @return
     */
    public RestTemplateClientHelper setContentType(MediaType mediaType){
        headers.setContentType(mediaType);
        return  this;
    }

    public void setClient(RestTemplate template) {
        this.template = template;
    }

    /**
     * 获取Spring Rest Template ,可以通过rest template 直接调用rest template 自带方法
     * @return
     */
    public RestTemplate getTemplate() {
        return template;
    }

    /**
     * 获取http header
     * @return
     */
    public HttpHeaders getHeaders() {
        return headers;
    }

    public RestTemplateClientHelper setHeaders(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 获取rest template helper的message converter，就是转换返回的不同类
     * @return
     */
    public List getMessageConverters() {
        return messageConverters;
    }

    /**
     * 设置Message Converter
     * @param messageConverters
     */
    public void setMessageConverters(List messageConverters) {
        this.messageConverters = messageConverters;
    }

    /**
     * 请求的http entity，包含了header，body
     * @return
     */
    public HttpEntity<T> getHttpEntity() {
        return httpEntity;
    }

    public void setHttpEntity(HttpEntity<T> httpEntity) {
        this.httpEntity = httpEntity;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public void setTemplate(RestTemplate template) {
        this.template = template;
    }

    @Deprecated
    public static List<String> getTestLoginHeader(String empNo){
        String urlLogin = "https://login.dooioo.net:18100/login?usercode="+empNo+"&password=12345";
        ResponseEntity response =  RestTemplateClientHelper.getHttpClientImplInstance().call(urlLogin, HttpMethod.POST);
        return response.getHeaders().get("set-cookie");
    }

    @Deprecated
    public static List<String> getIntegrationLoginHeader(String empNo){
        String urlLogin = "https://login.dooioo.org/login?usercode="+empNo+"&password=PW_12345";
        ResponseEntity response = RestTemplateClientHelper.getHttpClientImplInstance().call(urlLogin, HttpMethod.POST);
        return response.getHeaders().get("set-cookie");
    }

    public RestTemplateClientHelper removeHeader(String key){
        this.headers.remove(key);
        return this;
    }
}
