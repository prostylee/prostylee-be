package vn.prostylee;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@Slf4j
@SpringBootApplication
public class ProStyleeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProStyleeApplication.class, args);
		log.info("Server started at " + LocalDateTime.now());
	}

}
