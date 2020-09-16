package cn.wps.model.payment;

import org.json.JSONObject;

public class MemberAdd {
    public long result;
    public String msg;
    MemberAddData data;

    public static MemberAdd fromJsonObject(JSONObject jsonObject) {
        MemberAdd memberAdd = new MemberAdd();
        memberAdd.result = jsonObject.optLong("result");
        memberAdd.msg = jsonObject.optString("msg");

        JSONObject dataObject = jsonObject.optJSONObject("data");
        if (dataObject == null)
            return memberAdd;
        memberAdd.data = new MemberAddData();
        memberAdd.data.url = dataObject.optString("url");
        memberAdd.data.billno = dataObject.optString("billno");
        return memberAdd;
    }


}

class MemberAddData {
    public String url;
    public String billno;
}




