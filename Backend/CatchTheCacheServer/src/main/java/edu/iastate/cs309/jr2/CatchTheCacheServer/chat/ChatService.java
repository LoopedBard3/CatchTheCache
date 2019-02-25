package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;
import edu.iastate.cs309.jr2.CatchTheCacheServer.user.User;

@Service
public class ChatService {

	@Autowired
	ChatRepository chatRepo;

	/**
	 * Create new chat in our ChatRepository based on information in
	 * ChatCreateRequest
	 * 
	 * @param request ChatCreateRequest object to use for chat creation
	 * @return ChatCreateResponse object with success boolean
	 */
	public ResponseEntity<ChatCreateResponse> create(ChatCreateRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}

		ChatCreateResponse response = new ChatCreateResponse();
		Chat c = new Chat();
		c.updateChat(request);
		chatRepo.save(c);
		response.setSuccess(true);

		return new ResponseEntity<ChatCreateResponse>(response, HttpStatus.OK);
	}

	/**
	 * Get list of all chats in the database
	 * 
	 * @return List of all chats in the database
	 */
	public Iterable<Chat> getAllChats() {
		Iterable<Chat> chats = chatRepo.findAll();
		return chats;
	}

	/**
	 * Poll ChatRepository for chat with specified id
	 * 
	 * @param id Unique integer id to search for
	 * @return available information about the polled id
	 */
	public String findChatById(@PathVariable("chatId") int id) {
		Optional<Chat> results = chatRepo.findById(id);
		return results.get().toString();
	}
}
