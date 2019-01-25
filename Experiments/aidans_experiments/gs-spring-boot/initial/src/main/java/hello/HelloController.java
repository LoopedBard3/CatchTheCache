package hello;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class HelloController {
    
	@RequestMapping(value = "/")
    public String index() {
        return "Greetings!";
    }
	
	@RequestMapping(value = "/{id}")
    public String index(@PathVariable String id) {
        return "Greetings " + id;
    }
	
	@RequestMapping(value = "/user/{u}")
    public String index(@PathVariable User u) {
        return "Greetings!";
    }
    
}
