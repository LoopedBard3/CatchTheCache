package edu.iastate.cs309.jr2.CatchTheCacheServer.user;

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
class UserController {

	@Autowired
	UserService userService;

	@RequestMapping(method = RequestMethod.POST, path = "/users")
	public ResponseEntity<UserCreateResponse> post(@RequestBody UserCreateRequest request) {
		return userService.create(request);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/login")
	public ResponseEntity<UserLoginResponse> post(@RequestBody UserLoginRequest request) {
		return userService.loginUser(request);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/login/forgot")
	public ResponseEntity<UserQuestionResponse> post(@RequestBody UserQuestionRequest request) {
		return userService.getQuestion(request);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/login/reset")
	public ResponseEntity<UserResetPassResponse> post(@RequestBody UserResetPassRequest request) {
		return userService.resetPassword(request);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users")
	public List<String> getAll() {
		return userService.getAllUsers();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/authority/{auth}")
	public List<String> getAuthority(@RequestBody int auth) {
		return userService.getByAuthority(auth);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/admins")
	public List<String> getAdmins() {
		return userService.getAllAdmins();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/moderators")
	public List<String> getModerators() {
		return userService.getAllModerators();
	}

	@RequestMapping(method = RequestMethod.GET, path = "/users/{userId}")
	public String getById(@PathVariable("userId") int id) {
		return userService.findUserById(id);
	}

}
