package com.kafkacommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.kafkacommerce.gateway", "com.kafkacommerce.common"},exclude = {DataSourceAutoConfiguration.class})
public class KafkaCommerceGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaCommerceGatewayApplication.class, args);
    }

}
