package edu.iastate.cs309.jr2.CatchTheCacheServer.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;

@RestController
class CacheController {

	@Autowired
	CacheService cacheService;

	/**
	 * POST request on path /caches used to create a new cache
	 * 
	 * @param request CacheAddRequest object used in cache creation attempt
	 * @return CacheAddResponse object with status on creation attempt
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/caches")
	public ResponseEntity<CacheAddResponse> post(@RequestBody CacheAddRequest request) {
		return cacheService.create(request);
	}

	/**
	 * GET request on path /caches to return a list of all caches in the database
	 * 
	 * @return CacheListResponse object containing a list of all caches in the
	 *         database
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/caches")
	public ResponseEntity<CacheListResponse> get() {
		return cacheService.list();
	}

	/**
	 * GET request on /caches/{cacheId} path to return a cache by id
	 * 
	 * @param id Unique integer id to search for
	 * @return Chat object with specified id
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/caches/{id}")
	public String getById(@PathVariable int id) {
		return cacheService.findCacheById(id);
	}

	/**
	 * POST request on path /caches/m/{id} used to send a message to the cache chat
	 * 
	 * @param request MessageRequest
	 * @return MessageResponse object with status on message attempt
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/caches/m/{id}")
	public ResponseEntity<MessageResponse> postMessage(@PathVariable int id, @RequestBody MessageRequest request) {
		return cacheService.postMessage(id, request);
	}

	/**
	 * GET request on path /caches/m/{id} to return a list of all messages
	 * 
	 * @return MessageListResponse object with list of messages for this cache
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/caches/m/{id}")
	public ResponseEntity<MessageListResponse> getMessages(@PathVariable int id) {
		return cacheService.getMessages(id);
	}

}
