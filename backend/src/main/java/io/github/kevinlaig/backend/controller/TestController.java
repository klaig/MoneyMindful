package io.github.kevinlaig.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
public class TestController {

    @GetMapping("/public/hello")
    public String sayHello() {
        return "Hello, World!";
    }

    @GetMapping("/admin/secure-endpoint")
    public String secureEndpoint() {
        return "This is a secure endpoint";
    }
}