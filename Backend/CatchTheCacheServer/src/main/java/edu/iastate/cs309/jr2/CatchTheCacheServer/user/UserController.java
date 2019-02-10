package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {

	@Autowired
	UserRepository userRepo;

	@RequestMapping(method = RequestMethod.POST, path = "/users/new")
	public ResponseEntity<UserCreateResponse> saveUser(@RequestBody User u) {
		if (u == null) {
			throw new NullPointerException();
		}
		
		boolean canSave = true;
		
		UserCreateResponse response = new UserCreateResponse();
		
		if (validateUsername(u.getUsername())) { // if username is not alreadyF taken and meets requirements
			response.setValidUser(true);
		} else
			canSave = false;

		if (validatePassword(u.getPassword())) { // if password meets requirements
			response.setValidPass(true);
		} else
			canSave = false;

		int authority = u.getAuthority();
		
		if (!(authority == 1 || authority == 2)) { // assign user authority level
			u.setAuthority(0);
		}

		response.setMessage(
				"Username Valid: " + response.getValidUser() + "; Password Valid: " + response.getValidPass());
		if (canSave)
			userRepo.save(u);
		return new ResponseEntity<UserCreateResponse>(response, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/login")
	public ResponseEntity<UserLoginResponse> loginUser(@RequestBody User u) {
		if (u == null) {
			throw new NullPointerException();
		}
		
		UserLoginResponse response = new UserLoginResponse();
		
		if (validateLogin(u.getUsername(), u.getPassword())) {
			response.setSuccess(true);
			response.setMessage("Login Success");
		} else {
			response.setSuccess(false);
			response.setMessage("Invalid username or password");
		}
		
		return new ResponseEntity<UserLoginResponse>(response, HttpStatus.OK);
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

	/**
	 * Check if desired username is taken and meets the other requirements
	 * 
	 * @param username Username to check
	 * @return false if username is already taken, true otherwise
	 */
	private boolean validateUsername(String username) {
		return !userRepo.existsByUsername(username) && username.matches("^(?=.{3,}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
	}
	
	/**
	 * Check if desired password meets our specified requirements
	 * 
	 * @param password Password to check
	 * @return true if valid, false otherwise
	 */
	private boolean validatePassword(String password) {
		return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$");
	}
	
	/**
	 * Validate login credentials
	 * @param username Username for account
	 * @param password Password for account
	 * @return true if the username exists and the password is correct, false otherwise
	 */
	private boolean validateLogin(String username, String password) {
		if(!(userRepo.existsByUsername(username) && username.matches("^(?=.{3,}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$") && validatePassword(password))){
			return false;
		}
		if(userRepo.findByUsername(username).getPassword().equals(password))
			return true;
		else return false;
	}

}
