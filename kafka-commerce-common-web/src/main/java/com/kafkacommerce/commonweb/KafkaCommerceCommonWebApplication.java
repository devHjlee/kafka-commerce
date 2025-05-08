package com.kafkacommerce.commonweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.kafkacommerce.common", "com.kafkacommerce.commonweb"})
public class KafkaCommerceCommonWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaCommerceCommonWebApplication.class, args);
    }

}
