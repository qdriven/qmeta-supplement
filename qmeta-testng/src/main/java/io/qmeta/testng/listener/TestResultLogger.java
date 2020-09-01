package io.qmeta.testng.listener;

import cn.hutool.core.util.StrUtil;
import io.qmeta.testng.testng.TestStepLoggerListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>TestResultLogger class.</p>
 *
 * @author patrick
 * @version $Id: $Id
 */
public class TestResultLogger {

    private static final Logger logger = LogManager.getLogger(TestResultLogger.class.getName());


    private TestResultLogger() {
    }

    /**
     * <p>log.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public static void log(String message){
        logger.info("test_step={}",message);
        TestStepLoggerListener.getInstance().pushLog(message);
    }

    /**
     * <p>log.</p>
     *
     * @param marker a {@link java.lang.String} object.
     * @param keyInfos a {@link java.lang.String} object.
     */
    public static void log(String marker,String ...keyInfos){
        logger.info("test_step: "+marker,keyInfos);
        TestStepLoggerListener.getInstance().pushLog(convertMarker(marker, keyInfos));
    }

    public static void logWOTestStep(String marker,String ...keyInfos){
        logger.info("test_step: " + marker, keyInfos);
    }

    private static String convertMarker(String marker,Object ...keyInfos){
       try {
           if (keyInfos == null) return "";
           if (keyInfos.length == 1 && keyInfos[0] == null) return "";

           String msg = marker;

           for (Object message : keyInfos) {
               if (message != null) {
                   msg = msg.replaceFirst("\\{\\}", message.toString());
               }
           }
           return msg;
       }catch (Exception e){
           return StrUtil.EMPTY;
       }
    }

    /**
     * <p>log.</p>
     *
     * @param marker a {@link java.lang.String} object.
     * @param message a {@link java.lang.Object} object.
     */
    public static void log(String marker,Object ...message){
        logger.info("test_step:"+marker,message);
        TestStepLoggerListener.getInstance().pushLog(convertMarker(marker, message));
    }
    /**
     * <p>error.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public static void error(String message){
        logger.error("error_test_step={}", message);
    }

    /**
     * <p>error.</p>
     *
     * @param marker a {@link java.lang.String} object.
     * @param keyInfos a {@link java.lang.Object} object.
     */
    public static void error(String marker,Object ...keyInfos){
        logger.error("error_test_step="+marker,keyInfos);
        TestStepLoggerListener.getInstance().pushLog(convertMarker(marker, keyInfos));
    }

    /**
     * <p>error.</p>
     *
     * @param e a {@link java.lang.Throwable} object.
     */
    public static void error(Throwable e){
        logger.error("error ", e);
        if(e==null) return;
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage());
        sb.append("\n");
        for (Object o : e.getStackTrace()) {
            sb.append("\n");
            sb.append(o);
        }

        TestStepLoggerListener.getInstance().pushLog(sb.toString());
    }
    /**
     * <p>warn.</p>
     *
     * @param e a {@link java.lang.Throwable} object.
     */
    public static void warn(Throwable e){
        logger.warn("error=",e);
        TestStepLoggerListener.getInstance().pushLog(e);
    }

    /**
     * <p>warn.</p>
     *
     * @param message a {@link java.lang.String} object.
     */
    public static void warn(String message){
        logger.info("warn_test_step={}",message);
        TestStepLoggerListener.getInstance().pushLog(message);
    }
}
