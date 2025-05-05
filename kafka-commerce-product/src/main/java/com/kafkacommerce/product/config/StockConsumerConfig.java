package com.kafkacommerce.product.config;

import com.kafkacommerce.common.kafka.event.OrderCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class StockConsumerConfig {

    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> consumerFactory() {
        JsonDeserializer<OrderCreatedEvent> deserializer = new JsonDeserializer<>(OrderCreatedEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Kafka 서버 주소
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "stock-consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(productErrorHandler());
        return factory;
    }

    // DLT(Dead Letter Topic)에 발행할 메시지 템플릿
    @Bean
    @Qualifier("produceTemplate")
    public KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate(ProducerFactory<String, OrderCreatedEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        ProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(config);
        return new KafkaTemplate<>(factory);
    }
    // DLT(Dead Letter Topic)에 메시지 발행시 사용할 Producer 설정 세팅
    @Bean
    public ProducerFactory<String, OrderCreatedEvent> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    // 에러 핸들러 구현
    @Bean
    public CommonErrorHandler productErrorHandler() {
        DeadLetterPublishingRecoverer recover = new DeadLetterPublishingRecoverer(kafkaTemplate(producerFactory()));

        // 1초(1000MS) 간격으로 2회 재시도
        FixedBackOff fixedBackOff = new FixedBackOff(1000L, 2L);
        return new DefaultErrorHandler(recover, fixedBackOff);
    }
}
