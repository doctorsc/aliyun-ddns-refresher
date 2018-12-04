package info.shenc.aliyunddnsrefresher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = {"info.shenc"})
public class AliyunDdnsRefresherApplication {

    public static void main(String[] args) {
        SpringApplication.run(AliyunDdnsRefresherApplication.class, args);
    }
}
