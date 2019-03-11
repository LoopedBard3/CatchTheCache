package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;

@RestController
class UserController {

	@Autowired
	UserService userService;

	/**
	 * POST request on /users path to attempt creation of a new User
	 * 
	 * @param request UserCreateRequest object to be used in creating a new User
	 * @return UserCreateResponse object to return the status of the User creation
	 *         attempt
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/users")
	public ResponseEntity<UserCreateResponse> post(@RequestBody UserCreateRequest request) {
		return userService.create(request);
	}

	/**
	 * POST request on /login path to attempt account login
	 * 
	 * @param request UserLoginRequest object to be used in our login attempt
	 * @return UserLoginResponse object to return the status of the login attempt
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/login")
	public ResponseEntity<UserLoginResponse> post(@RequestBody UserLoginRequest request) {
		return userService.loginUser(request);
	}

	/**
	 * POST request on /login/forgot path to retrieve security question
	 * 
	 * @param request UserQuestionRequest object to be used to retrieve security
	 *                question
	 * @return UserQuestionResponse object to return the security question
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/login/forgot")
	public ResponseEntity<UserQuestionResponse> post(@RequestBody UserQuestionRequest request) {
		return userService.getQuestion(request);
	}

	/**
	 * POST request on /login/request path to reset user password
	 * 
	 * @param request UserResetPassRequest object to be used in password reset
	 *                attempt
	 * @return UserResetPassResponse object to return the status of the password
	 *         reset
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/login/reset")
	public ResponseEntity<UserResetPassResponse> post(@RequestBody UserResetPassRequest request) {
		return userService.resetPassword(request);
	}

	/**
	 * GET request on /users path to return a list of all taken usernames
	 * 
	 * @return List of strings containing all taken usernames
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users")
	public List<String> getAll() {
		return userService.getAllUsers();
	}

	/**
	 * GET request on /users/authority path to return a list of all users with a
	 * specified authority level
	 * 
	 * @param auth integer authority level to return
	 * @return List of strings of all usernames with authority level auth
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users/authority/{auth}")
	public List<String> getAuthority(@RequestBody int auth) {
		return userService.getByAuthority(auth);
	}

	/**
	 * GET request on path /users/admins to return a list of all administrators
	 * 
	 * @return List of strings of all usernames with administrator permissions
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users/admins")
	public List<String> getAdmins() {
		return userService.getAllAdmins();
	}

	/**
	 * GET request on path /users/moderators to return a list of all moderators
	 * 
	 * @return List of strings of all usernames with moderator permissions
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users/moderators")
	public List<String> getModerators() {
		return userService.getAllModerators();
	}

	/**
	 * GET request on path /users to retrieve a user by their unique id
	 * 
	 * @param id Unique integer id to search for
	 * @return String description of the user with the specified id
	 */
	@RequestMapping(method = RequestMethod.GET, path = "/users/{userId}")
	public String getById(@PathVariable("userId") int id) {
		return userService.findUserById(id);
	}

}
