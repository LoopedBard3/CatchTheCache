package edu.iastate.cs309.jr2.CatchTheCacheServer.system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class WelcomeController {

	@GetMapping("/welcome/")
	public String welcome() {
		return "CONNECTION ESTABLISHED";
	}
}