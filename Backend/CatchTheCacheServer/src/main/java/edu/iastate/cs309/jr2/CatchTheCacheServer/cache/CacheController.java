package edu.iastate.cs309.jr2.CatchTheCacheServer.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	@RequestMapping(method = RequestMethod.POST, path = "/caches")
	public ResponseEntity<CacheAddResponse> post(@RequestBody CacheAddRequest request) {
		return cacheService.create(request);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/caches")
	public ResponseEntity<CacheListResponse> get(@RequestBody CacheListRequest request) {
		return cacheService.list(request);
	}

}
