package io.qmeta.testng.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

/**
 * Created by patrick on 15/3/2.
 *
 * @version $Id$
 */


public class ChineseUtils {
    private static final Logger logger = LogManager.getLogger(ChineseUtils.class.getName());
    private static Random random = new Random(new Date().getTime());
    private ChineseUtils(){}

    /**
     * 获取单个中文字符
     * @return
     */
    public static String getSingleChineseCharactor(){
        String str =null;
        int highPos,lowPos;
        highPos = 176 + Math.abs(random.nextInt(39));
        lowPos=161+Math.abs(random.nextInt(93));
        byte[] b = new byte[2];
        b[0] = Integer.valueOf(highPos).byteValue();
        b[1]=Integer.valueOf(lowPos).byteValue();

        try {
            str = new String(b,"GB2312");
        } catch (UnsupportedEncodingException e) {
            logger.error("error_result={}",e);
        }

        return str;
    }

    /**
     * 获取随机字符串
     * @param length
     * @return
     */
    public static String getFixedLengthChinese(int length){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(ChineseUtils.getSingleChineseCharactor());
        }

        return sb.toString();
    }

    /**
     * 获取长度在某个区间内的中文字符串
     * @param min
     * @param max
     * @return
     */
    public static String getRandomLengthChinese(final int min,final int max){
        int length = Math.abs(random.nextInt(max-min))+min;
        return getFixedLengthChinese(length);
    }

    /**
     * 获取格式是xx（2-4位）路XX(1-9999)号
     * @return
     */
    public static String getRandomAddress(){
        return String.format("%s路%d号",getRandomLengthChinese(2,4),random.nextInt(9998)+1);
    }
}
