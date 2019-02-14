package edu.iastate.cs309.jr2.CatchTheCacheServer.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;
import edu.iastate.cs309.jr2.CatchTheCacheServer.user.*;

@Service
public class CacheService {

	@Autowired
	CacheRepository cacheRepo;
	UserRepository userRepo;

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
		boolean locationAvailable = !cacheRepo.existsByLocation(request.getLatitude(), request.getLongitude());
		// Check creator authorization
		// TODO: VERIFY THIS IS LEGAL
		boolean authorized = userRepo.findByUsername(request.getCreator()).getAuthority() == 2;

		response.setAuthorized(authorized);
		response.setSuccess(nameAvailable && locationAvailable);

		return new ResponseEntity<CacheAddResponse>(response, HttpStatus.OK);
	}

}
