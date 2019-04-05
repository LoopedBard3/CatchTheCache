package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;
import io.micrometer.shaded.org.pcollections.HashPMap;

@RestController
class ChatController {
	private int MAX_CHAT_HISTORY;
	
	private SimpMessagingTemplate template;
	
	private Map<String, Object> msgCache = new HashMap<String, Object>();
	@Autowired
	ChatService chatService;
	
	
	/**
	 * Method to set the maximum chat stored in buffer
	 * @param MAX_CHAT_HISTORY size of maximum buffer
	 */
	public void setMAX_CHAT_HISTORY(int MAX_CHAT_HISTORY) {
		this.MAX_CHAT_HISTORY = MAX_CHAT_HISTORY;
	}
	
	/**
	 * POST request on /chats path to attempt creation of a new Chat object
	 * 
	 * @param request ChatCreateRequest object to be used in creating a new Chat
	 * @return ChatCreateResponse object to return the status of the Chat creation
	 *         attempt
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/chats")
	public ResponseEntity<ChatCreateResponse> post(@RequestBody ChatCreateRequest request) {
		return chatService.create(request);
	}

	/**
	 * GET request on /chats path to return a list of all taken chats
	 * 
	 * @return List of strings containing all chats in the database
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/chats")
	public Iterable<Chat> getAll() {
		return chatService.getAllChats();
	}

	/**
	 * GET request on /chats path to return a chat by id
	 * 
	 * @param id Unique integer id to search for
	 * @return Chat object with specified id
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/chats/{chatId}")
	public String getById(@PathVariable("chatId") int id) {
		return chatService.findChatById(id);
	}

	@MessageMapping("/chat")
	@SendTo("/topic/messages")
	public OutputMessage send(Message message) throws Exception {
	    String time = new SimpleDateFormat("HH:mm").format(new Date());
	    return new OutputMessage(message.getSender(), message.getText(), time);
	}
	
	@MessageMapping("/userChat")
	public void userChat(Message chatMessage) {
		
		String dest = "/userChat/chat" + chatMessage.getChatId();
		
		Object cache = msgCache.get(chatMessage.getChatId());
			chatMessage.setChatid(URLDecoder.decode(chatMessage.getChatId(),"utf-8"));
			chatMessage.setUsers(URLDecoder.decode(chatMessage.getUsers(), "utf-8"));
			chatMessage.setText(URLDecoder.decode(chatMessage.getText(), "utf-8"));
		
			
		this.template.convertAndSend(dest, chatMessage);
                ((LimitQueue<Message>) cache).offer(chatMessage);
	}


	

}
