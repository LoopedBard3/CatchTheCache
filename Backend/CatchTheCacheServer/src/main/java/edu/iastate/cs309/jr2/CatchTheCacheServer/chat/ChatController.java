package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;

@RestController
class ChatController {

	@Autowired
	ChatService chatService;

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
	public List<Chat> getAll() {
		return chatService.getAllChats();
	}

	/**
	 * GET request on /chats/{id} path to return a chat by id
	 * 
	 * @param id Unique integer id to search for
	 * @return Chat object with specified id
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/chats/{id}")
	public String getById(@PathVariable int id) {
		return chatService.findChatById(id);
	}

	/**
	 * POST request on path /chats/m/{id} used to send a message to the cache chat
	 * 
	 * @param request MessageRequest
	 * @return MessageResponse object with status on message attempt
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/chats/m/{id}")
	public ResponseEntity<MessageResponse> postMessage(@PathVariable int id, @RequestBody MessageRequest request) {
		return chatService.postMessage(id, request);
	}

	/**
	 * GET request on path /chats/m/{id} to return a list of all messages
	 * 
	 * @return MessageListResponse object with list of messages for this cache
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/chats/m/{id}")
	public ResponseEntity<MessageListResponse> getMessages(@PathVariable int id) {
		return chatService.getMessages(id);
	}
	
	@RequestMapping (method = RequestMethod.POST, path = "/chats/{id}/")
	public ResponseEntity<ChatAddUserResponse> addUser(@PathVariable int id, @RequestBody ChatAddUserRequest request){
		
		return chatService.addUser(id, request);
	}
	
	@RequestMapping (method = RequestMethod.POST, path = "/chats/{id}/")
	public ResponseEntity<ChatRemoveUserResponse> removeUser(@PathVariable int id, @RequestBody ChatRemoveUserRequest request){
		
		return chatService.removeUser(id, request);
	}
}
