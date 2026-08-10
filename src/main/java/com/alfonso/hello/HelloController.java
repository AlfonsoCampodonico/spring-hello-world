package com.alfonso.hello;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP adapter. Parses the request and delegates; holds no logic of its own.
 */
@RestController
public class HelloController {

    private final Greeter greeter;

    HelloController(Greeter greeter) {
        this.greeter = greeter;
    }

    @GetMapping("/")
    Map<String, String> hello(@RequestParam(required = false) String name) {
        return Map.of("message", greeter.greet(name));
    }

    @GetMapping("/health")
    Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
