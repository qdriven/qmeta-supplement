package io.qmeta.http.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qmeta.http.context.LocalRetrofitContext;
import io.qmeta.http.context.RetrofitContext;
import io.qmeta.http.interceptors.RetryInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.util.concurrent.TimeUnit.MINUTES;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@ConditionalOnClass(Retrofit.class)
@EnableConfigurationProperties(QMetaHttpConfig.class)
public class QMetaHttpAutoConfiguration {

    private final List<Converter.Factory> converterFactories;

    private final OkHttpClient okHttpClient;

    private final QMetaHttpConfig retrofitProperties;

    @Autowired
    public QMetaHttpAutoConfiguration(List<Converter.Factory> converterFactories, OkHttpClient okHttpClient,
                                      QMetaHttpConfig retrofitProperties) {
        this.converterFactories = converterFactories;
        this.okHttpClient = okHttpClient;
        this.retrofitProperties = retrofitProperties;
        checkConfiguredUrl(this.retrofitProperties);
    }

    @Slf4j
    @Configuration
    @ConditionalOnClass(OkHttpClient.class)
    public static class OkHttpClientConfiguration {

        /**
         * Mark this as a bean for user to easy monitor the connection status
         */
        @Bean
        @ConditionalOnMissingBean
        public ConnectionPool connectionPool(QMetaHttpConfig properties) {
            QMetaHttpConfig.Connection connection = properties.getConnection();
            return new ConnectionPool(connection.getMaxIdleConnections(), connection.getKeepAliveDuration(), MINUTES);
        }

        @Bean
        @ConditionalOnMissingBean
        public OkHttpClient okHttpClient(QMetaHttpConfig properties, ConnectionPool connectionPool, List<Interceptor> interceptors) {
            QMetaHttpConfig.Connection connection = properties.getConnection();

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .readTimeout(connection.getReadTimeout(), TimeUnit.MILLISECONDS)
                    .writeTimeout(connection.getWriteTimeout(), TimeUnit.MILLISECONDS)
                    .connectTimeout(connection.getConnectTimeout(), TimeUnit.MILLISECONDS)
                    .connectionPool(connectionPool);

            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }

            return builder.build();
        }

        @Configuration
        @ConditionalOnClass(Interceptor.class)
        public static class InterceptorConfiguration {

            @Bean
            @ConditionalOnClass(HttpLoggingInterceptor.class)
            @ConditionalOnProperty(value = "retrofit.log.enabled", havingValue = "true")
            public HttpLoggingInterceptor loggingInterceptor(QMetaHttpConfig properties) {
                QMetaHttpConfig.Log logConf = properties.getLog();
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(innerLogger(logConf.getLevel(), OkHttpClientConfiguration.log));
                interceptor.setLevel(logConf.getContent());
                return interceptor;
            }

            @Bean
            public RetryInterceptor retryInterceptor(QMetaHttpConfig properties) {
                return new RetryInterceptor(properties.getConnection().getRetryTimes());
            }

            private HttpLoggingInterceptor.Logger innerLogger(Level level, Logger logger) {
                if (level == Level.DEBUG) {
                    return logger::debug;
                } else if (level == Level.ERROR) {
                    return logger::error;
                } else if (level == Level.INFO) {
                    return logger::info;
                } else if (level == Level.TRACE) {
                    return logger::trace;
                } else if (level == Level.WARN) {
                    return logger::warn;
                }
                throw new UnsupportedOperationException("We don't support this log level currently.");
            }
        }
    }

    @Configuration
    @ConditionalOnClass(JacksonConverterFactory.class)
    public static class JacksonConverterFactoryConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public ObjectMapper mapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
            return mapper;
        }

        @Bean
        @ConditionalOnMissingBean
        public JacksonConverterFactory jacksonConverterFactory(ObjectMapper mapper) {
            mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            return JacksonConverterFactory.create(mapper);
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public RetrofitContext retrofitContext() {
        Builder builder = new Builder().validateEagerly(true);
        converterFactories.forEach(builder::addConverterFactory);
        if (okHttpClient != null) {
            builder.client(okHttpClient);
        }
        RetrofitContext context = new LocalRetrofitContext();
        retrofitProperties.getEndpoints()
                .forEach(endPoint -> context.register(endPoint.getIdentity(), builder.baseUrl(endPoint.getBaseUrl()).build()));
        return context;
    }

    /**
     * Check the configured url format is valid by using {@code new URI()}
     */
    private void checkConfiguredUrl(QMetaHttpConfig properties) {
        properties.getEndpoints().stream()
                .map(QMetaHttpConfig.EndPoint::getBaseUrl)
                .forEach(url -> {
                    Assert.isTrue(ResourceUtils.isUrl(url), url + " is not a valid url");
                    if (!url.endsWith("/")) {
                        log.warn("The [{}] didn't end with \"/\". This means a relative base url, end with / would be better.", url);
                    }
                });
    }
}
