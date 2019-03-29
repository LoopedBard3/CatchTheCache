package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

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
	

}
