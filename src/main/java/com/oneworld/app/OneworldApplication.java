package com.oneworld.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class OneworldApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneworldApplication.class, args);
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        System.out.println("Seeding Database...");

    }
}
