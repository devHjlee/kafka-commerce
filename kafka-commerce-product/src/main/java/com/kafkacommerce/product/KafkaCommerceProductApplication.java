package com.kafkacommerce.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kafkacommerce.common", "com.kafkacommerce.product"})
public class KafkaCommerceProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaCommerceProductApplication.class, args);
    }

}
