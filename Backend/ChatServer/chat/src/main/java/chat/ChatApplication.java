package chat;

import java.util.List;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import chat.repository.Chat;
import chat.repository.ChatRepository;

@SpringBootApplication
public class ChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatApplication.class, args);
		
		
	}
	@RestController
	@RequestMapping("/api")
	public class ApiController {
		
		@Autowired
		private ChatRepository chatRepository;
		
		 @ResponseStatus(HttpStatus.OK)
		    @GetMapping(value = "/v1/chat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
		    public List<Chat> getAll() {
		        return chatRepository.findAll();
		    }
		@GetMapping("/greeting")
		public String GetGreeting() {
			return "Hello World from the API";
		}
	}
	
}
