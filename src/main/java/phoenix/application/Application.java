package phoenix.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

//@ComponentScan("phoenix")
//@EntityScan("phoenix")
@SpringBootApplication
public class Application {

    @Bean
    public Random random() {
        return new Random();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
