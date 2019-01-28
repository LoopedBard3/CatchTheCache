package restlogintest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
class LoadDatabase {

	@Bean
	CommandLineRunner initDatabase(UserRepository repository) {
		return args -> {
			log.info("Preloading " + repository.save(new User("pbibus@example.com", "iloveminecraft")));
			log.info("Preloading " + repository.save(new User("aidans@example.com", "1337h4x0rz")));
		};
	}
}