package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;

@Service
public class ChatService {

	@Autowired
	ChatRepository chatRepo;
	@Autowired
	MessageRepository messageRepo;

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
	public List<Chat> getAllChats() {
		List<Chat> chats = chatRepo.findAll();
		return chats;
	}

	/**
	 * Poll ChatRepository for chat with specified id
	 * 
	 * @param id Unique integer id to search for
	 * @return available information about the polled id
	 */
	public String findChatById(int id) {
		Optional<Chat> results = chatRepo.findById(id);
		return results.get().toString();
	}

	/**
	 * Create new chat for a cache and return the chat id associated with the new
	 * chat
	 * 
	 * @return new Chat object
	 */
	public Chat createChatForCache() {
		Chat c = new Chat();
		chatRepo.save(c);
		return c;
	}

	/**
	 * Post new message to chat room
	 * 
	 * @param id      specific chat room to use
	 * @param request MessageRequest object containing sender and message strings
	 * @return MessageResponse with success boolean
	 */
	public ResponseEntity<MessageResponse> postMessage(int id, MessageRequest request) {
		MessageResponse response = new MessageResponse();
		Message m = new Message();
		m.setSender(request.getSender());
		m.setText(request.getMessage());
		m.setChatId(id);
		if (m.getChatId() != null) {
			response.setSuccess(true);
			messageRepo.saveAndFlush(m);
		}
		return new ResponseEntity<MessageResponse>(response, HttpStatus.OK);
	}

	/**
	 * Get messages for the cache chat room with id
	 * 
	 * @param id cache chat room to get
	 * @return MessageListResponse with List of Message objects.
	 */
	public ResponseEntity<MessageListResponse> getMessages(int id) {
		List<Message> l;
		l = messageRepo.findAllByChatId(id);
		MessageListResponse response = new MessageListResponse(l);
		return new ResponseEntity<MessageListResponse>(response, HttpStatus.OK);
	}
	
	/**
	 * Get Chat by chat Id
	 * @param id Integer number that specifies the id to look for
	 * @return The chat that is found, or null if the chat does not exist
	 */
	public Chat getChatById(int id) {
		Optional<Chat> results = chatRepo.findById(id);
		return results.get();
	}
}
