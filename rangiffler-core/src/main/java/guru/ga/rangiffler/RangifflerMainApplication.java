package guru.ga.rangiffler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RangifflerMainApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(RangifflerMainApplication.class);
        springApplication.run(args);
    }
}