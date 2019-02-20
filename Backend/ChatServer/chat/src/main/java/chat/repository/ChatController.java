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
import java.util.Optional;
@RestController
public class ChatController {

	    @Autowired
	    ChatService chatService;
	    @Autowired
	    ChatRepository chatRepository;
	    
	    @RequestMapping(method = RequestMethod.POST, path = "/chats/new")
	    public String saveChat(Chat chat) {
	    	System.out.println(chat.toString());
	        chatRepository.save(chat);
	        return chat.toString() ;
	    }
	    
	    @RequestMapping(method = RequestMethod.GET, path = "/chats")
	    public List<Chat> getAllChats() {   
	        List<Chat> results = chatRepository.findAll();
	        return results;
	    }

	    @RequestMapping(method = RequestMethod.GET, path = "/chats/{chatId}")
	    public Chat findChatById(@PathVariable("chatId") int chatId) {
	       
	        Chat results = chatRepository.findByChatId(chatId);
	        return results;
	    }
	    
	    @RequestMapping(path = "/", method = RequestMethod.GET)
	    @ResponseBody
	    public Chat defaultCreate(String name) {
	        return new Chat(1, "new Chat",null);
	    }
	  		
//	    @RequestMapping(path = "/chats", method= RequestMethod.GET)
//		public List<Chat> get() {
//		    return chatService.getAll();
//		}
		
//		@RequestMapping(path="/chats/{chatId}",method=RequestMethod.GET)
//		public Chat getById(@PathVariable ("chatId")Integer chatId) {
//		    return chatService.getById(chatId);
//		}
		
	    
		@RequestMapping(path = "/response", method = RequestMethod.POST)
	    public @ResponseBody ResponseEntity<ChatCreateResponse> postResponseController(
	    		ChatCreateRequest request) {
			System.out.println(request.toString());
	        return chatService.create(request);
	     }
		
		
	    @RequestMapping(path = "/", method = RequestMethod.POST)
	    public ResponseEntity<Chat> update(@RequestBody Chat chat) {

	        if (chat != null) {
	            chat.setUser(chat.getUser() + " newUser");
	        }

	        // TODO: call persistence layer to update
	        return new ResponseEntity<Chat>(chat, HttpStatus.OK);
	    }
	
}
