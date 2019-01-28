package org.springframework.samples.petclinic.system;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class WelcomeController {

    @GetMapping("/test")
    public String welcome() {
        return "welcome";
    }
}
