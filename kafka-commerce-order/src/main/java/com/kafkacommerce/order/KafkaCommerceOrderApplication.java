package com.kafkacommerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kafkacommerce.common", "com.kafkacommerce.order"})
public class KafkaCommerceOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaCommerceOrderApplication.class, args);
    }

}
