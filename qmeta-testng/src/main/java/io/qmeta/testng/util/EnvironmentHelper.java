package io.qmeta.testng.util;

import io.ift.automation.Environment;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
* Created by patrick on 15/3/3.
*
* @version $Id$
*/


public class EnvironmentHelper {
    private static final Logger logger = LogManager.getLogger(EnvironmentHelper.class.getName());
    private static Map<String,Environment> environment = Maps.newConcurrentMap();
    private EnvironmentHelper(){}
    public static final String DEFAULT_APPNAME=PropertiesHelper.getProperty("default.appname", "fangyuan").toLowerCase();
    private static final String JDBC_URL=".jdbc.url";
    private static final String JDBC_DRIVER=".jdbc.driver";
    private static final String JDBC_USERNAME=".jdbc.username";
    private static final String JDBC_PASSWORD=".jdbc.password";
    private static final String SERVICE_ROOTPATH =".service.root";
    private static final String HOME_URL=".home.url";
    private static final String DOMAIN_URL=".domain.url";
    private static final String OPENAPI_ROOTPATH=".openapi.root";
    private static final String TARGET_ENVIRONMENT_KEY="target.environment";
    private static String name = PropertiesHelper.getSystemFirstProperty(TARGET_ENVIRONMENT_KEY, "integration");

    static{
        environment.put(DEFAULT_APPNAME,new Environment(name,name +"."+DEFAULT_APPNAME+JDBC_URL,name +"."+DEFAULT_APPNAME+
                JDBC_DRIVER,name + "."+DEFAULT_APPNAME+JDBC_USERNAME,name
                + "."+DEFAULT_APPNAME+JDBC_PASSWORD,name+"."+DEFAULT_APPNAME+ SERVICE_ROOTPATH,name+HOME_URL
                ,name+OPENAPI_ROOTPATH,name+"."+DEFAULT_APPNAME+DOMAIN_URL));
        logger.info("environment {} is initialized", environment.toString());
    }

    public static String getTargetEnvironmentName(){
        return name;
    }

    public static String getDbURL(){
        return environment.get(DEFAULT_APPNAME).getDbURL();
    }

    public static String getDbDriver(){
        return environment.get(DEFAULT_APPNAME).getDbDriver();
    }

    public static String getDbUserName(){
        return environment.get(DEFAULT_APPNAME).getDbUserName();
    }

    public static String getDbPassword(){
        return environment.get(DEFAULT_APPNAME).getDbPassword();
    }

    public static String getServiceRootPath(){
        return environment.get(DEFAULT_APPNAME).getServiceROOTPath();
    }

    public static String getHomeUrl(){
        return environment.get(DEFAULT_APPNAME).getHomeUrl();
    }

    public static String getOpenApiRootPath(){
        return environment.get(DEFAULT_APPNAME).getOpenAPIRoot();
    }

    public static String getApiRootByApiDomain(String apiDomainName) {
        return get(apiDomainName).getServiceROOTPath();
    }

    public static Environment getEnvironment() {
        return environment.get(DEFAULT_APPNAME);
    }


    public static String getDomainUrl(){
        return environment.get(DEFAULT_APPNAME).getDomainUrl();
    }

    //quick fix, move to a mapping for better performance;
    public static synchronized Environment get(String appName) {
        appName = appName.toLowerCase();
        if (environment.get(appName)==null){
            environment.put(appName,new Environment(name,name +"."+appName+ JDBC_URL,name +"."+appName+
                    JDBC_DRIVER,name + "."+appName+JDBC_USERNAME,name
                    + "."+appName+JDBC_PASSWORD,name+"."+appName+ SERVICE_ROOTPATH,name+HOME_URL
                    ,name+OPENAPI_ROOTPATH,name+"."+appName+DOMAIN_URL));
        }
        return environment.get(appName);
    }

}
