package com.thinkdifferent.convertvideo;

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.annotation.Resource;

@SpringBootApplication
@EnableOpenApi
public class ConvertvideoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ConvertvideoApplication.class, args);
        RabbitMQStart rabbitMQRun = context.getBean(RabbitMQStart.class);
        rabbitMQRun.start();
    }

    @Bean
    public RabbitMQStart rabbitMQRun() {
        return new RabbitMQStart();
    }
    private static class RabbitMQStart {
        //为了在main中的static方法中使用@value注解只能用这种办法
        @Value("${rabbitmq.start}")
        private Boolean rabbitmqStart;

        @Resource
        RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
        public void start() {
            if(rabbitmqStart)
                rabbitListenerEndpointRegistry.start();
            else
                rabbitListenerEndpointRegistry.stop();
            System.out.println("=================== Rabbitmq:"+rabbitmqStart+"===================");
        }
    }

}
