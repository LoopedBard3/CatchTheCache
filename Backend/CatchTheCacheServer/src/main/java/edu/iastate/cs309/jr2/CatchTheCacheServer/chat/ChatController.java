package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;

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
	
	@MessageMapping("/userChat")
	public void userChat(Message chatMessage) {
		
		String dest = "/userChat/chat" + chatMessage.getChatId();
		
		LimitQueue<Message> buffer = (LimitQueue<Message>) msgCache.get(chatMessage.getChatId());
			chatMessage.setChatId(chatMessage.getChatId());
			chatMessage.setSender(chatMessage.getSender());
			chatMessage.setText(chatMessage.getText());
		
			
		this.template.convertAndSend(dest, chatMessage);
               buffer.offer(chatMessage);
	}

	@SubscribeMapping("/initChat/{chatId}")
	public LimitQueue<Message> initChatRoom(@DestinationVariable String chatId) {
		System.out.print("New User Entering Chat");
		LimitQueue<Message> chatlist = new LimitQueue<Message>(MAX_CHAT_HISTORY);
		
		//If never entered in this chat
		if (!msgCache.containsKey(chatId)) {
			msgCache.put(chatId, chatlist);
		} else {
			chatlist = (LimitQueue<Message>) msgCache.get(chatId);
		}
		return chatlist;
	}


	

}
