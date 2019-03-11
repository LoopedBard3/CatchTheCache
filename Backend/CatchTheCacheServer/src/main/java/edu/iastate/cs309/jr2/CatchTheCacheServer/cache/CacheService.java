package edu.iastate.cs309.jr2.CatchTheCacheServer.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;
import edu.iastate.cs309.jr2.CatchTheCacheServer.user.*;
import edu.iastate.cs309.jr2.CatchTheCacheServer.chat.*;

@Service
public class CacheService {

	@Autowired
	CacheRepository cacheRepo;
	@Autowired
	MessageRepository messageRepo;
	@Autowired
	UserRepository userRepo;
	@Autowired
	ChatRepository chatRepo;
	@Autowired
	ChatService chatService;

	/**
	 * Create new cache in our CacheRepository based on information in
	 * CacheAddRequest
	 * 
	 * @param request CacheAddRequest object to use for cache creation attempt
	 * @return CacheAddResponse object with success and authorized booleans
	 */
	public ResponseEntity<CacheAddResponse> create(CacheAddRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}
		CacheAddResponse response = new CacheAddResponse();
		// Check name
		boolean nameAvailable = !cacheRepo.existsByName(request.getName());
		// Check location
		boolean locationAvailable = true;// !cacheRepo.existsByLocation(request.getLatitude(), request.getLongitude());
		// Check creator authorization
		boolean authorized = userRepo.findByUsername(request.getCreator()).getAuthority() == 2;

		response.setAuthorized(authorized);
		response.setSuccess(authorized && nameAvailable && locationAvailable);

		if (response.getAuthorized() && response.getSuccess()) {
			Cache c = new Cache();
			c.updateCache(request);
			Chat ch = chatService.createChatForCache();
			c.setChatId(ch.getId());
			cacheRepo.save(c);
			ch.setCacheId(c.getId());
			chatRepo.flush();
		}

		return new ResponseEntity<CacheAddResponse>(response, HttpStatus.OK);
	}

	/**
	 * Create new cache in our CacheRepository based on information in
	 * CacheAddRequest
	 * 
	 * @param request CacheAddRequest object to use for cache creation attempt
	 * @return CacheAddResponse object with success and authorized booleans
	 */
	public ResponseEntity<CacheListResponse> list() {

		ArrayList<Cache> list = new ArrayList<>();

		list.addAll(cacheRepo.findAll());

		CacheListResponse response = new CacheListResponse(list);

		return new ResponseEntity<CacheListResponse>(response, HttpStatus.OK);
	}

	/**
	 * Post new message to cache chat room
	 * 
	 * @param id      specific cache chat room to use
	 * @param request MessageRequest object containing sender and message strings
	 * @return MessageResponse with success boolean
	 */
	public ResponseEntity<MessageResponse> postMessage(int id, MessageRequest request) {
		MessageResponse response = new MessageResponse();
		Message m = new Message();
		m.setSender(request.getSender());
		m.setText(request.getMessage());
		Cache c = cacheRepo.findById(id).get();
		m.setChatId(c.getChatId());
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
		Cache c = cacheRepo.findById(id).get();
		l = messageRepo.findAllByChatId(c.getChatId());
		MessageListResponse response = new MessageListResponse(l);
		return new ResponseEntity<MessageListResponse>(response, HttpStatus.OK);
	}

	/**
	 * Poll CacheRepository for cache with specified id
	 * 
	 * @param id Unique integer id to search for
	 * @return available information about the polled id
	 */
	public String findCacheById(int id) {
		Optional<Cache> results = cacheRepo.findById(id);
		return results.get().toString();
	}

}
