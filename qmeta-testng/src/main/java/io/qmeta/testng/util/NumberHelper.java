package io.qmeta.testng.util;

import java.math.BigDecimal;

/**
 * Created by patrick on 15/11/25.
 */
public class NumberHelper {

    private NumberHelper(){

    }

    /**
     * 保留两位小数,roundup
     * @param value
     * @return
     */
    public static Double parseDouble2Digital(String value){
        BigDecimal bd = new BigDecimal(value);
        BigDecimal  bd2 = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        return Double.parseDouble(bd2.toString());
    }

    public static Double parseDouble2Digital(Double value){
        BigDecimal bd = new BigDecimal(value);
        BigDecimal  bd2 = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        return Double.parseDouble(bd2.toString());
    }

    /**
     * 保留给定位小数,roundup
     * @param value
     * @param scale 保留小数位数
     * @return
     */
    public static Double parseDoubleToGivenScale(String value,int scale){
        BigDecimal bd = new BigDecimal(value);
        BigDecimal  bd2 = bd.setScale(scale,BigDecimal.ROUND_HALF_UP);
        return Double.parseDouble(bd2.toString());
    }

    /**
     * 保留给定位小数,roundupModel,传入
     * @param value
     * @param scale 保留小数位数
     * @param roundUpModel 保留小数位数
     * @return
     */
    public static Double parseDoubleToGivenScale(String value,int scale,int roundUpModel){
        BigDecimal bd = new BigDecimal(value);
        BigDecimal  bd2 = bd.setScale(scale,roundUpModel);
        return Double.parseDouble(bd2.toString());
    }
}
