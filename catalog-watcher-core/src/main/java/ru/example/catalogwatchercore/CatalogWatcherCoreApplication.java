package ru.example.catalogwatchercore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan({"ru.example", "ru.example.catalogwatcherdb"})
public class CatalogWatcherCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogWatcherCoreApplication.class, args);
    }
}
