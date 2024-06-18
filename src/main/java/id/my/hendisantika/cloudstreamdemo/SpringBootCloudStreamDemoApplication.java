package id.my.hendisantika.cloudstreamdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.function.Supplier;

@SpringBootApplication
public class SpringBootCloudStreamDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCloudStreamDemoApplication.class, args);
    }

    @Bean
    public Supplier<Flux<Long>> producer() {
        return () -> Flux.interval(Duration.ofSeconds(1))
                .log();
    }
}
