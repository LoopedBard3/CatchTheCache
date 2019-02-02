package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {

	@Autowired
	UserRepository userRepo;

	@RequestMapping(method = RequestMethod.POST, path = "/users/new")
	public String saveUser(User u) {
		userRepo.save(u);
		return "New user " + u.getUsername() + " saved";
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users")
	public List<User> getAllUsers() {
		List<User> results = userRepo.findAll();
		return results;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/admins")
	public List<User> getAllAdmins() {
		List<User> results = userRepo.findAllByAuthority(2);
		return results;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/moderators")
	public List<User> getAllModerators() {
		List<User> results = userRepo.findAllByAuthority(1);
		results.addAll(getAllAdmins());
		return results;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/{userId}")
	public Optional<User> findOwnerById(@PathVariable("userId") int id) {
		Optional<User> results = userRepo.findById(id);
		return results;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/check")
	public boolean checkUser(User u) {
		return userRepo.existsByUsername(u.getUsername());
	}

}
