package io.github.kevinlaig.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("/api/admin/secure-endpoint")
    public String secureEndpoint() {
        return "This is a secure endpoint";
    }
}