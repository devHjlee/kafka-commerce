package com.kafkacommerce.product.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class DLTProducerConfig {

    /**
     * DLT(Dead Letter Topic)에 메시지 발행시 사용할 Producer 설정
     * @return ProducerFactory 설정
     */
    @Bean
    public ProducerFactory<String, Object> dltProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    /**
     * DLT(Dead Letter Topic)에 발행할 메시지 템플릿
     * @param producerFactory ProducerFactory 빈
     * @return KafkaTemplate 설정
     */
    @Bean
    public KafkaTemplate<String, Object> dltKafkaTemplate(ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    /**
     * DLT 에러 핸들러 구현
     * @param dltKafkaTemplate DLT용 KafkaTemplate
     * @return DefaultErrorHandler 설정
     */
    @Bean
    public DefaultErrorHandler dltErrorHandler(KafkaTemplate<String, Object> dltKafkaTemplate) {
        DeadLetterPublishingRecoverer recover = new DeadLetterPublishingRecoverer(dltKafkaTemplate);
        // 1초(1000MS) 간격으로 2회 재시도
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 2L);
        return new DefaultErrorHandler(recover, fixedBackOff);
    }
}
