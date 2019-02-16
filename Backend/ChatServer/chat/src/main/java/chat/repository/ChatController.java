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
	    public ChatController(ChatRepository repo) {
	    	this.chatRepository=repo;
	    }
	    
	    @GetMapping("/")
	    @ResponseBody
	    public Chat sayHello(String name) {
	        return new Chat(1, "new Chat",null);
	    }
	  		
	    @GetMapping("/chats")
		public List<Chat> get() {
		    return chatRepository.findAll();
		}
		
		@GetMapping("/chats/{id}")
		public Chat get(@PathVariable Integer id) {
		    return chatService.getById(id);
		}
		
		@RequestMapping(method = RequestMethod.POST, path = "/chats")
		public ResponseEntity postController(
		  @RequestBody ChatCreateRequest request) {
		    return chatService.create(request);
		}
	    
		@RequestMapping(path = "/response", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, 
		        produces = {MediaType.APPLICATION_ATOM_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	    public @ResponseBody ResponseEntity postResponseController(
	    		ChatCreateRequest request) {
	        return chatService.create(request);
	     }
		
		@RequestMapping(value= "/post", method = RequestMethod.POST)
		 public ResponseTransfer create(@RequestBody Chat chat) {
			chatRepository.save(chat);
			return new ResponseTransfer("Thanks For Posting!!!");
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
