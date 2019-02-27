package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

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

@Service
public class UserService {

	@Autowired UserRepository userRepo;

	/**
	 * Create new user in our UserRepository based on information in
	 * UserCreateRequest
	 * 
	 * @param request UserCreateRequest object to use for account creation attempt
	 * @return UserCreateResponse object with success, validUser, and validPass
	 *         booleans, and message String
	 */
	public ResponseEntity<UserCreateResponse> create(UserCreateRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}

		UserCreateResponse response = new UserCreateResponse();

		response.setValidUser(validateUsername(request.getUsername()));
		response.setValidPass(validatePassword(request.getPassword()));
		response.setMessage(
				"Username Valid: " + response.getValidUser() + "; Password Valid: " + response.getValidPass());
		response.setSuccess(response.getValidUser() && response.getValidPass());

		if (response.getSuccess()) {
			User u = new User();
			u.updateUser(request);
			userRepo.save(u);
		}

		return new ResponseEntity<UserCreateResponse>(response, HttpStatus.OK);
	}

	/**
	 * Query UserRepository and attempt login with given credentials
	 * 
	 * @param request UserLoginRequest object to use for login attempt
	 * @return UserLoginResponse object with success boolean and message String
	 */
	public ResponseEntity<UserLoginResponse> loginUser(UserLoginRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}

		UserLoginResponse response = new UserLoginResponse();

		if (validateLogin(request.getUsername(), request.getPassword())) {
			response.setSuccess(true);
			response.setMessage("Login Success");
		} else {
			response.setSuccess(false);
			response.setMessage("Invalid username or password");
		}

		return new ResponseEntity<UserLoginResponse>(response, HttpStatus.OK);
	}

	/**
	 * Request security question for user
	 * 
	 * @param request UserQuestionRequest object to use
	 * @return UserQuestionResponse object with security question
	 */
	public ResponseEntity<UserQuestionResponse> getQuestion(UserQuestionRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}

		User u = userRepo.findByUsername(request.getUsername());
		UserQuestionResponse response = new UserQuestionResponse(u.getSecurityQuestion());

		return new ResponseEntity<UserQuestionResponse>(response, HttpStatus.OK);
	}

	/**
	 * Attempt to update user password if they have the correct answer to the
	 * security question
	 * 
	 * @param request UserResetPassRequest object to use
	 * @return UserResetPassResponse object with success boolean and message String
	 */
	public ResponseEntity<UserResetPassResponse> resetPassword(UserResetPassRequest request) {
		if (request == null) {
			throw new NullPointerException();
		}

		User u = userRepo.findByUsername(request.getUsername());
		UserResetPassResponse response = new UserResetPassResponse();
		
		response.setValidAnswer(request.getAnswer().equals(u.getSecurityAnswer()));
		response.setValidPassword(validatePassword(request.getNewPassword()));
		response.setMessage(
				"Answer Valid: " + response.getValidAnswer() + "; Password Valid: " + response.getValidPass());

		if (response.getSuccess()) {
			u.setPassword(request.getNewPassword());
			userRepo.saveAndFlush(u);
		}

		return new ResponseEntity<UserResetPassResponse>(response, HttpStatus.OK);
	}

	/**
	 * Get list of all accounts
	 * 
	 * @return List of all usernames in the database
	 */
	public List<String> getAllUsers() {
		List<User> users = userRepo.findAll();
		List<String> results = new ArrayList<String>();
		for (User u : users) {
			results.add(u.getUsername());
		}
		return results;
	}

	/**
	 * Get list of all accounts with authority auth
	 * 
	 * @param auth Authority level to get
	 * @return List of all usernames in the database
	 */
	public List<String> getByAuthority(int auth) {
		if (auth > 2 || auth < 0)
			throw new IllegalArgumentException();
		List<User> users = userRepo.findAllByAuthority(auth);
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
	 * Poll UserRepository for user with specified id
	 * 
	 * @param id Unique integer id to search for
	 * @return available information about the polled id
	 */
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
		if(username == null) {
			throw new NullPointerException();
		}
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
		if(password == null) {
			throw new NullPointerException();
		}
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
