package cn.wps.model.yunfile;

import org.json.JSONObject;

public class RemainingSpace {
    public long remaining;
    public long result;

    public static RemainingSpace fromJsonObject(JSONObject jsonObject)
    {
        RemainingSpace remainingSpace = new RemainingSpace();
        remainingSpace.result = jsonObject.optLong("result");
        remainingSpace.remaining = jsonObject.optLong("remaining");

        return remainingSpace;
    }
}
