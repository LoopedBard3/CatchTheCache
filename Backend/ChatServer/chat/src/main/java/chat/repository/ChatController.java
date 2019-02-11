package chat.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class ChatController {

	    @Autowired
	    private ChatRepository chatRepository;

	    @ResponseStatus(HttpStatus.OK)
	    @GetMapping(value = "/v1/chat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public List<Chat> getAll() {
	        return chatRepository.findAll();
	    }
	    
	    @RequestMapping(value="/")
	    public ResponseEntity<Chat> get(){
	    	Chat chat= new Chat();
	    	chat.chatid=0;
	    	chat.user="Default";
	    	chat.cacheID=null;
	    	return new ResponseEntity<Chat>(chat,HttpStatus.OK);
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
