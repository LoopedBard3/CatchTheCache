package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;
import edu.iastate.cs309.jr2.CatchTheCacheServer.user.*;

@Service
public class ChatService {

	@Autowired
	ChatRepository chatRepo;
	@Autowired
	UserRepository userRepo;
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
	/**
	 * Check whether user exists from UserReopsitory and not already in chat, then add to the specified chat
	 * @param id The chat id to remove user from
	 * @param request
	 * @return ChatAddUserResponse with success or not
	 */
	
	public ResponseEntity<ChatAddUserResponse> addUser(int id, ChatAddUserRequest request){
		//Boolean value to check whether the user can be added to chat
		boolean canAdd= true;
		
		//If user or chat does not exist in repository, the user is not able to added to chat
		if(!userRepo.existsByUsername(request.getUsername())|| !chatRepo.existsById(id) )
			canAdd= false;
				
		//Find the User by username
		User user = userRepo.findByUsername(request.getUsername());
		Chat chat = chatRepo.findById(id).get();
			
		//Check whether user already exists in chat
		
		if(chat.hasUser(user))
			canAdd=false;
		
		ChatAddUserResponse response = new ChatAddUserResponse();
		
		if(canAdd)
			response.setSuccess(chat.addUser(user));
		
		return new ResponseEntity<ChatAddUserResponse>(response, HttpStatus.OK);
	}
	
	public ResponseEntity<ChatRemoveUserResponse> removeUser(int id, ChatRemoveUserRequest request){
		//Boolean value to check whether the user can be removed from chat
		boolean canRemove= true;
		
		//If user or chat does not exist in repository, the user is not able to added to chat
		if(!userRepo.existsByUsername(request.getUsername())|| !chatRepo.existsById(id) )
			canRemove= false;
		
		//Find the User by username and find chat from corresponding repositories.
		User user = userRepo.findByUsername(request.getUsername());
		Chat chat = chatRepo.findById(id).get();
			
		//Check whether user already exists in chat
		if(!chat.hasUser(user))
			canRemove=false;
		
		ChatRemoveUserResponse response = new ChatRemoveUserResponse();
		
		if(canRemove)
			response.setSuccess(chat.removeUser(user));
			
		
		return new ResponseEntity<ChatRemoveUserResponse>(response, HttpStatus.OK);
	}
}
