package cn.wps.openapi;

import cn.wps.exception.YunException;
import cn.wps.http.JSONBuilder;
import cn.wps.http.Request;
import cn.wps.http.RequestBody;
import cn.wps.model.payment.CustomPay;
import cn.wps.model.payment.MemberAdd;
import cn.wps.model.payment.BannerOpen;

public class Payment {

    //判断用户是否有相关权益
    public static boolean getUsableService(String accessToken, String openid, String serviceId, long totalNum) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("openid", openid);
        jsonBuilder.put("service_id", serviceId);
        jsonBuilder.put("total_num", totalNum);
        jsonBuilder.put("appid", Config.getAppid());
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/vas/service/usable")
                .post(new RequestBody(jsonBuilder));
        return request.execute().getJSONObject().optInt("result", 0) == 0;
    }

    //预下单接口
    public static String preorderPay(String accessToken, String openid, String serviceId, long totalNum, String billno, String subject,
                                     String position, String clientIp) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("openid", openid);
        jsonBuilder.put("service_id", serviceId);
        jsonBuilder.put("total_num", totalNum);
        jsonBuilder.put("billno", billno);
        jsonBuilder.put("subject", subject);
        jsonBuilder.put("position", position);
        jsonBuilder.put("client_ip", clientIp);
        jsonBuilder.put("appid", Config.getAppid());
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/vas/pay/preorder")
                .post(new RequestBody(jsonBuilder));
        if (request.execute().getJSONObject().optInt("result", 0) == 0)
            return billno;
        return "";
    }

    //使用用户自身权益
    public static boolean useService(String accessToken, String openid, String serviceId, long totalNum, String billno) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("openid", openid);
        jsonBuilder.put("service_id", serviceId);
        jsonBuilder.put("total_num", totalNum);
        jsonBuilder.put("billno", billno);
        jsonBuilder.put("appid", Config.getAppid());
        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/vas/service/use")
                .post(new RequestBody(jsonBuilder));
        return request.execute().getJSONObject().optInt("result", 0) == 0;
    }

    //零售下单
    public static CustomPay customOrderPay(String accessToken, String billno, String openid, String payment, String serviceId, String subject,
                                        String position, long totalFee, long count) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("billno", billno);
        jsonBuilder.put("openid", openid);
        jsonBuilder.put("payment", payment);
        jsonBuilder.put("service_id", serviceId);
        jsonBuilder.put("subject", subject);
        jsonBuilder.put("position", position);
        jsonBuilder.put("total_fee", totalFee);
        jsonBuilder.put("count", count);

        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/vas/pay/customorder")
                .post(new RequestBody(jsonBuilder));
        return CustomPay.fromJsonObject(request.execute().getJSONObject());
    }

    //添加会员
    public static MemberAdd memberAdd(String accessToken, String openid, String orderid, int memberid, int days,
                                      String phone) throws YunException {
        JSONBuilder jsonBuilder = new JSONBuilder();
        jsonBuilder.put("access_token", accessToken);
        jsonBuilder.put("appid", Config.getAppid());
        jsonBuilder.put("openid", openid);
        jsonBuilder.put("orderid", orderid);
        jsonBuilder.put("memberid", memberid);
        jsonBuilder.put("days", days);
        jsonBuilder.put("phone", phone);

        Request request = new Request().url(Config.getOpenApiServer() + "/oauthapi/v2/vas/pay/member/add")
                .post(new RequestBody(jsonBuilder));
        return MemberAdd.fromJsonObject(request.execute().getJSONObject());
    }

    //开放标签
    public static BannerOpen bannerOpen(String accessToken, String mod, String position) throws YunException {
        String requestUrl = Config.getOpenApiServer() + "/oauthapi/v2/vas/banner/open?appid=" + Config.getAppid() + "&access_token=" + accessToken + "&mod=" + mod + "&position=" + position;
        Request request = new Request().url(requestUrl)
                .get();

        return BannerOpen.fromJsonObject(request.execute().getJSONObject());
    }

}
