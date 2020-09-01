package io.qmeta.supplement.random;

import cn.hutool.core.util.RandomUtil;

import java.awt.*;
import java.text.DecimalFormat;

public class QRandomUtil extends RandomUtil {

    public static int getNumber$right(int a, int b) {
        // 计算差值
        int bound = a > b ? a - b : b - a;
        return RandomUtil.getRandom().nextInt(bound + 1) + a;
    }

    /* ——————————————————————— getCode : 获取随机code(字母和数字) ——————————————————————————— */

    /**
     * 获取随机code，包含数字和字母
     *
     * @param length
     * @return
     */
    public static String getCode(int length) {
        StringBuilder s = new StringBuilder();
        for (int i = 1; i <= length; i++) {
            if (RandomUtil.getRandom().nextBoolean()) {
                // 0.5的概率为0-9的数字
                s.append(RandomUtil.getRandom().nextInt());
            } else {
                // 0.5的概率为字母，其中大写0.25，小写0.25
                if (RandomUtil.getRandom().nextBoolean()) {
                    // 小写
                    s.append(getRandomChar());
                } else {
                    // 大写
                    s.append((getRandomChar() + "").toUpperCase());
                }
            }
        }
        return s.toString();
    }

    /**
     * 获取一个4位数的随机code，字母小写
     *
     * @return
     */
    public static String getCode() {
        return getCode(4);
    }

    /* ——————————————————————— getUUID : 获取随机UUID,java.util自带的UUID方法 ——————————————————————————— */

    /* ——————————————————————— getRandomChar : 获取随机字符（单个字母） ——————————————————————————— */

    /**
     * 获取一个随机英文字符，小写
     *
     * @return
     */
    public static char getRandomChar() {
        return (char) (RandomUtil.getRandom().nextInt(26) + 97);
    }

    /* ———————————————————— getRandomString : 获取随机字符串 ———————————————————————— */

    /**
     * 获取一串指定长度的随机字符串
     *
     * @param length     字符串长度
     * @param randomCase 是否开启随机大小写
     * @return
     */
    public static String getRandomString(int length, boolean randomCase) {
        char[] crr = new char[length];
        for (int i = 0; i < length; i++) {
            Character randomChar = getRandomChar();
            // 如果开启了随机大写，则有概率将字符转为大写 1/2

            if (randomCase) {
                crr[i] =
                        RandomUtil.getRandom().nextBoolean() ? randomChar : Character.toUpperCase(randomChar);
            } else {
                crr[i] = randomChar;
            }
        }
        return new String(crr);
    }

    /**
     * 获取一串指定长度的随机字符串，默认大小写随机
     *
     * @param length 字符串长度
     * @return
     */
    public static String getRandomString(int length) {
        return getRandomString(length, true);
    }

    /**
     * 获取一串长度为32的字符串，默认大小写随机
     *
     * @return
     */
    public static String getRandomString() {
        return getRandomString(32, true);
    }

    /**
     * 数字小数保留
     *
     * @param dnum   需要保留的小数
     * @param length 小数保留位数
     * @return
     */
    public static String toFixed(Number dnum, int length) {
        StringBuilder sb = new StringBuilder("#.");
        // 遍历并设置位数
        for (int i = 0; i < length; i++) {
            sb.append("0");
        }

        // 返回结果
        String douStr = numFormat(dnum, sb.toString());
        if (douStr.startsWith(".")) {
            // 如果开头是点，说明首位是0，补位
            douStr = "0" + douStr;
        }
        return douStr;
    }

    /**
     * 自定义数字格式化
     *
     * @param dnum
     * @param formatStr
     * @return
     */
    public static String numFormat(Number dnum, String formatStr) {
        return new DecimalFormat(formatStr).format(dnum);
    }

    /* ———————————————————— getColor : 获取随机颜色 ———————————————————————— */

    /**
     * 返回一个随机颜色
     *
     * @return
     */
    public static Color randomColor() {
        int[] arr = randomColor$intArr();
        return new Color(arr[0], arr[1], arr[2]);
    }

    /**
     * 返回一个长度为三的数组，三位分别代表了颜色的R、G、B
     *
     * @return
     */
    public static int[] randomColor$intArr() {
        final int[] arr = new int[3];
        arr[0] = RandomUtil.getRandom().nextInt(256);
        arr[1] = RandomUtil.getRandom().nextInt(256);
        arr[2] = RandomUtil.getRandom().nextInt(256);
        return arr;
    }

    /**
     * 返回16进制颜色代码
     *
     * @return
     */
    public static String randomColor$hexString() {
        int[] arr = randomColor$intArr();
        StringBuilder sb = new StringBuilder();
        String r = Integer.toHexString(arr[0]);
        r = r.length() == 1 ? '0' + r : r;

        String g = Integer.toHexString(arr[1]);
        g = g.length() == 1 ? '0' + g : g;

        String b = Integer.toHexString(arr[2]);
        b = b.length() == 1 ? '0' + b : b;
        sb.append("#").append(r).append(g).append(b);
        return sb.toString();
    }

    /* ———————————————————— getProbability : 根据概率获取boolean ———————————————————————— */

    /**
     * 根据概率获取boolean，区间：[probL , probR]
     *
     * @param probL 概率百分比区间的左参数，取值范围为0-1之间，对应了0%和100%
     * @param probR 概率百分比区间的右参数，取值范围为0-1之间，对应了0%和100%
     * @return
     */
    public static Boolean getProbability(double probL, double probR) {
        double v = RandomUtil.getRandom().nextDouble();
        if (v >= probL && v <= probR) {
            return true;
        }
        return false;
    }

    /**
     * 根据概率获取boolean，区间：[0 , prob] 填入的参数即为概率的百分比
     *
     * @param prob 概率百分比的小数形式，参数范围0-1
     * @return
     */
    public static Boolean getProbability(double prob) {
        return getProbability(0, prob);
    }
}
