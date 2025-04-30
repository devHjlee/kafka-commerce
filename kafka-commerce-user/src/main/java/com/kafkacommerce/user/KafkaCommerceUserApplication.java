package com.kafkacommerce.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kafkacommerce.common", "com.kafkacommerce.user"})
public class KafkaCommerceUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaCommerceUserApplication.class, args);
    }

}
