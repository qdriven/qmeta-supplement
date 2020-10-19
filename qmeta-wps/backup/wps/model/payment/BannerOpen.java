package cn.wps.model.payment;

import org.json.JSONObject;


public class BannerOpen {
    public long result;
    public String msg;
    BannerData data;

    public static BannerOpen fromJsonObject(JSONObject jsonObject) {
        BannerOpen bannerOpen = new BannerOpen();
        bannerOpen.result = jsonObject.optLong("result");
        bannerOpen.msg = jsonObject.optString("msg");

        JSONObject dataObject = jsonObject.optJSONObject("data");
        if (dataObject == null)
            return bannerOpen;
        bannerOpen.data = new BannerData();
        bannerOpen.data.blueButton = dataObject.optString("blue_button");
        bannerOpen.data.btnMsg = dataObject.optString("btn_msg");
        bannerOpen.data.btnName = dataObject.optString("btn_name");
        bannerOpen.data.extra = dataObject.optString("extra");
        bannerOpen.data.icon = dataObject.optString("icon");
        bannerOpen.data.jumpMod = dataObject.optString("jump_mod");
        bannerOpen.data.labelName = dataObject.optString("label_name");
        bannerOpen.data.link = dataObject.optString("link");
        bannerOpen.data.panelLink = dataObject.optString("panel_link");
        bannerOpen.data.priorityType = dataObject.optString("priority_type");
        bannerOpen.data.sceneid = dataObject.optString("sceneid");
        bannerOpen.data.seceneid = dataObject.optString("seceneid");
        bannerOpen.data.showMod = dataObject.optString("show_mod");
        bannerOpen.data.title = dataObject.optString("title");
        bannerOpen.data.type = dataObject.optString("type");
        return bannerOpen;
    }
}

class BannerData {
    public String blueButton;
    public String btnMsg;
    public String btnName;
    public String extra;
    public String icon;
    public String jumpMod;
    public String labelName;
    public String link;
    public String panelLink;
    public String priorityType;
    public String sceneid;
    public String seceneid;
    public String showMod;
    public String title;
    public String type;
}
