package ai.flowx.quickstart.connector.config;

import ai.flowx.quickstart.connector.exception.ExchangeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Slf4j
@Configuration
@EnableKafka
public class KafkaConfiguration {

    private ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

    @Value(value = "${kafka.consumer.threads}")
    private Integer threadsNumber;

    @Value(value = "${kafka.auth-exception-retry-interval}")
    private Long authorizationExceptionRetryInterval;

    @PostConstruct
    private void configure() {
        initializeThreadPoolExecutor(executor, "kafka-connector-thread-group", "KafkaConnectorConsumerThread", threadsNumber);
    }

    private void initializeThreadPoolExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor, String threadGroupName, String threadNamePrefix, Integer threadsNumbers){
        threadPoolTaskExecutor.setThreadGroupName(threadGroupName);
        threadPoolTaskExecutor.setCorePoolSize(threadsNumbers);
        log.info("Configuring kafka consumers thread pool for group " + threadGroupName + " with " + threadPoolTaskExecutor.getCorePoolSize() + " threads.");

        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.initialize();
    }

    @Bean
    public SeekToCurrentErrorHandler errorHandler() {
        SeekToCurrentErrorHandler handler = new SeekToCurrentErrorHandler();
        handler.addNotRetryableExceptions(ExchangeException.class);
        return handler;
    }

    @Bean
    public StringJsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new StringJsonMessageConverter(objectMapper);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> listenerContainerFactory(ConsumerFactory consumerFactory, RecordMessageConverter messageConverter, SeekToCurrentErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setMessageConverter(messageConverter);
        factory.setConcurrency(threadsNumber);
        factory.setErrorHandler(errorHandler);
        factory.getContainerProperties().setConsumerTaskExecutor(executor);
        factory.setContainerCustomizer(
                container -> container.getContainerProperties().setAuthorizationExceptionRetryInterval(
                        Duration.ofSeconds(authorizationExceptionRetryInterval)));
        return factory;
    }
}
