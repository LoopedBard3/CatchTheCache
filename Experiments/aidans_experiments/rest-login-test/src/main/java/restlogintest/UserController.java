package restlogintest;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UserController {

	private final UserRepository repository;

	UserController(UserRepository repository) {
		this.repository = repository;
	}

	// Aggregate root

	@GetMapping(value = "/")
    public String index() {
        return "Welcome to Prometheus";
    }
	
	// tag::get-aggregate-root[]
	@GetMapping("/users")
	Resources<Resource<User>> all() {

		List<Resource<User>> users = repository.findAll().stream()
			.map(user -> new Resource<>(user,
				linkTo(methodOn(UserController.class).one(user.getId())).withSelfRel(),
				linkTo(methodOn(UserController.class).all()).withRel("users")))
			.collect(Collectors.toList());
		
		return new Resources<>(users,
			linkTo(methodOn(UserController.class).all()).withSelfRel());
	}
	// end::get-aggregate-root[]

	@PostMapping("/users")
	User newUser(@RequestBody User newUser) {
		return repository.save(newUser);
	}

	// Single item

	// tag::get-single-item[]
	@GetMapping("/users/{id}")
	Resource<User> one(@PathVariable Long id) {
		
		User user = repository.findById(id)
			.orElseThrow(() -> new UserNotFoundException(id));
		
		return new Resource<>(user,
			linkTo(methodOn(UserController.class).one(id)).withSelfRel(),
			linkTo(methodOn(UserController.class).all()).withRel("users"));
	}
	// end::get-single-item[]

	@PutMapping("/users/{id}")
	User replaceUser(@RequestBody User newUser, @PathVariable Long id) {
		
		return repository.findById(id)
			.map(user -> {
				user.setUsername(newUser.getUsername());
				user.setPasswd(newUser.getPasswd());
				return repository.save(user);
			})
			.orElseGet(() -> {
				newUser.setId(id);
				return repository.save(newUser);
			});
	}

	@DeleteMapping("/users/{id}")
	void deleteUser(@PathVariable Long id) {
		repository.deleteById(id);
	}
}
