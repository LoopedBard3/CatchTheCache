package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

import java.util.ArrayList;
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

	/**
	 * Create new user in our UserRepository based on information in
	 * UserCreateRequest
	 * 
	 * @param u UserCreateRequest object to use for account creation attempt
	 * @return UserCreateResponse object with success, validUser, and validPass
	 *         booleans, and message String
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/users/new")
	public ResponseEntity<UserCreateResponse> saveUser(@RequestBody UserCreateRequest u) {
		if (u == null) {
			throw new NullPointerException();
		}

		boolean canSave = true;

		UserCreateResponse response = new UserCreateResponse();

		if (validateUsername(u.getUsername())) { // if username is not already taken and meets requirements
			response.setValidUser(true);
		} else
			canSave = false;

		if (validatePassword(u.getPassword())) { // if password meets requirements
			response.setValidPass(true);
		} else
			canSave = false;

		response.setMessage(
				"Username Valid: " + response.getValidUser() + "; Password Valid: " + response.getValidPass());
		if (canSave) {
			response.setSuccess(true);
			User toSave = new User(); // create new user from request and save it to our UserRepository
			toSave.updateUser(u);
			userRepo.save(toSave);
		}
		return new ResponseEntity<UserCreateResponse>(response, HttpStatus.OK);
	}

	/**
	 * Query UserRepository and attempt login with given credentials
	 * 
	 * @param u UserLoginRequest object to use for login attempt
	 * @return UserLoginResponse object with success boolean and message String
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/login")
	public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest u) {
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

	/**
	 * Get list of all accounts
	 * 
	 * @return List of all usernames in the database
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users")
	public List<String> getAllUsers() {
		List<User> users = userRepo.findAll();
		List<String> results = new ArrayList<String>();
		for (User u : users) {
			results.add(u.getUsername());
		}
		return results;
	}

	/**
	 * Get list of accounts with administrator permissions (authority == 2)
	 * 
	 * @return List of Strings of usernames with authority == 2
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users/admins")
	public List<String> getAllAdmins() {
		List<User> users = userRepo.findAllByAuthority(2);
		List<String> results = new ArrayList<String>();
		for (User u : users) {
			results.add(u.getUsername());
		}
		return results;
	}

	/**
	 * Get list of accounts with moderation permissions (authority > 0)
	 * 
	 * @return List of Strings of usernames with authority > 0
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users/moderators")
	public List<String> getAllModerators() {
		List<User> users = userRepo.findAllByAuthority(1);
		List<String> results = new ArrayList<String>();
		for (User u : users) {
			results.add(u.getUsername());
		}
		results.addAll(getAllAdmins());
		return results;
	}

	/**
	 * Poll UserRepository for user with specified ID
	 * 
	 * @param id the ID of the user to find
	 * @return available information about the polled ID
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users/{userId}")
	public String findUserById(@PathVariable("userId") int id) {
		Optional<User> results = userRepo.findById(id);
		return results.get().toString();
	}

	/**
	 * Check if desired username is taken and meets the other requirements
	 * 
	 * @param username Username to check
	 * @return false if username is already taken, true otherwise
	 */
	private boolean validateUsername(String username) {
		return !userRepo.existsByUsername(username)
				&& username.matches("^(?=.{3,}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
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
	 * 
	 * @param username Username for account
	 * @param password Password for account
	 * @return true if the username exists and the password is correct, false
	 *         otherwise
	 */
	private boolean validateLogin(String username, String password) {
		if (!(userRepo.existsByUsername(username)
				&& username.matches("^(?=.{3,}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$")
				&& validatePassword(password))) {
			return false;
		}
		if (userRepo.findByUsername(username).getPassword().equals(password))
			return true;
		else
			return false;
	}

}
