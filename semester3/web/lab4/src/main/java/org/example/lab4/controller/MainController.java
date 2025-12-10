package org.example.lab4.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/home")
public class MainController {

    @GetMapping
    public String example() {
        return "Hello, world!";
    }

}