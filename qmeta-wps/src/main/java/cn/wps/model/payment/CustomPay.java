package cn.wps.model.payment;

import org.json.JSONObject;

public class CustomPay {
    public long result;
    public String msg;
    CustomPayData data;

    public static CustomPay fromJsonObject(JSONObject jsonObject) {
        CustomPay customPay = new CustomPay();
        customPay.result = jsonObject.optLong("result");
        customPay.msg = jsonObject.optString("msg");

        JSONObject dataObject = jsonObject.optJSONObject("data");
        if (dataObject == null)
            return customPay;
        customPay.data = new CustomPayData();
        customPay.data.url = dataObject.optString("url");
        customPay.data.billno = dataObject.optString("billno");
        customPay.data.orderNum = dataObject.optString("order_num");
        customPay.data.totalFee = dataObject.optString("total_fee");
        return customPay;
    }
}

class CustomPayData {
    public String url;
    public String billno;
    public String orderNum;
    public String totalFee;
}
