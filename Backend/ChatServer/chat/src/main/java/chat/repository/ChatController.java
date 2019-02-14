package chat.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class ChatController {

	    @Autowired
	    ChatService chatService;
	    private ChatRepository chatRepository;
	    
	    @GetMapping("/")
	    @ResponseBody
	    public Chat sayHello(@RequestParam(name="name", required=false, defaultValue="Stranger") String name) {
	        return new Chat(1, "new Chat",null);
	    }
	  		
	    @GetMapping("/chats")
		List<Chat> get() {
		    return chatService.getAll();
		}
		
		@GetMapping("/chats/{id}")
		Chat get(@PathVariable int id) {
		    return chatService.getById(id);
		}

		@PostMapping("/users")
		Chat post(@RequestBody Chat user) {
		    return chatService.create(user);
		}
	    
	    @RequestMapping(value = "/", method = RequestMethod.POST)
	    public ResponseEntity<Chat> update(@RequestBody Chat chat) {

	        if (chat != null) {
	            chat.setUser(chat.getUser() + " newUser");
	        }

	        // TODO: call persistence layer to update
	        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
	    }
	
}
