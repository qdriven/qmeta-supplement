package io.qmeta.testng.util;

import com.google.common.base.Function;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelper extends StringUtils {
    private static final Logger logger = LogManager.getLogger(StringHelper.class);

    private StringHelper() {
    }

    /**
     * m5 给定字符串
     *
     * @param data
     * @return
     */
    public static String md5(String data) {
        try {
            return org.apache.commons.codec.digest.DigestUtils.md5Hex(data.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.error("getMD5_error={}", e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 根据条件连接字符传
     *
     * @param stringArray
     * @param function
     * @param separator   分割符
     * @return
     */
    public static String join(String[] stringArray, Function<String, String> function, String separator) {
        StringBuilder sb = new StringBuilder();
        for (String s : stringArray) {
            sb.append(function.apply(s));
            sb.append(separator);
        }
        return sb.substring(0, sb.length() - separator.length());
    }

    /**
     * 根据条件连接字符传
     *
     * @param stringArray
     * @param function    转换数组的函数
     * @return
     */
    public static String join(String[] stringArray, Function<String, String> function) {
        StringBuilder sb = new StringBuilder();
        for (String s : stringArray) {
            sb.append(function.apply(s));
        }
        return sb.substring(0, sb.length());
    }


    /**
     * 根据正则表达式取子字符串，从匹配字符开始的地方取字符串
     *
     * @param target            目标字符串
     * @param regexp            正则表达式
     * @param fromIndex         第N个匹配的地方
     * @param includeRegExpFlag 包括匹配字符，或者不包括匹配字符
     * @return
     */
    public static String subStringFromMatcher(String target, String regexp,
                                              int fromIndex, boolean includeRegExpFlag) {
        Pattern p = Pattern.compile(regexp);
        Matcher matcher = p.matcher(target);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            i++;
            if (i == fromIndex) {
                int index = includeRegExpFlag ? matcher.start() : matcher.end();
                sb.append(target.substring(index, target.length()));
                break;
            }

        }
        return sb.toString();
    }

    /**
     * Big Decimal RoundUp to String, keep 2 digital
     *
     * @param number
     * @return
     */
    public static String bigDecimalRoundUpToString(BigDecimal number) {

        if (number.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        number = number.setScale(2, BigDecimal.ROUND_HALF_UP);
        return number.toString();
    }

    /**
     * return formated money number which is round up and keep 2 digital
     *
     * @param moneyNum
     * @return
     */
    public static String formatMoneyNumber(String moneyNum) {
        BigDecimal d = new BigDecimal(moneyNum);
        return bigDecimalRoundUpToString(d);
    }

    /**
     * convert UTF-8 characters
     *
     * @param input
     * @return
     */
    public static String toUTF8String(String input) {
        try {
            return new String(input.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return input;
        }
    }

    /**
     * 检查字符串不是null,或者只有空格，或者长度是0
     *
     * @param source
     * @return
     */
    public static boolean isNotEmptyOrNotBlankString(String source) {
        if (source != null) source = source.trim();
        return StringUtils.isNotEmpty(source) || StringUtils.isNotBlank(source);
    }

    public static boolean isNoneContentString(String source) {
        return !isNotEmptyOrNotBlankString(source);
    }

    /**
     * 根据驼峰命名分割字符串
     *
     * @param camel
     * @return
     */
    public static String splitCamelCase(String camel) {
        return WordUtils.capitalizeFully(camel.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        ));
    }

    /**
     * 根据正则表达式抽取数据,如果没有匹配则返回空字符串
     *
     * @param source  源数据
     * @param pattern 正则表达式
     * @return 如果没有匹配则返回空字符串
     */
    public static String extract(String source, String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
        Matcher matcher = p.matcher(source);
        if (matcher.find()) {
            return matcher.group();
        }
        return StringHelper.EMPTY;
    }


}
