package io.qmeta.http.config;

import lombok.Data;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.event.Level;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;


@Data
@ConfigurationProperties(prefix = "qmeta.http")
public class QMetaHttpConfig {

    public static int HTTP_RETRY_TIMES = 5;
    private Connection connection = new Connection();
    private List<EndPoint> endpoints = new ArrayList<>();
    private Log log = new Log();

    @Data
    public static class EndPoint {
        private String identity;
        private String baseUrl;
    }

    @Data
    public static class Connection {

        private Long readTimeout = 10000L;

        private Long writeTimeout = 10000L;

        private Long connectTimeout = 10000L;

        private Integer maxIdleConnections = 5;

        private Integer keepAliveDuration = 5;

        private int retryTimes = 5;
    }

    @Data
    public static class Log {
        private Boolean enabled = false;
        private HttpLoggingInterceptor.Level content = HttpLoggingInterceptor.Level.BODY;
        private Level level = Level.DEBUG;
    }
}
