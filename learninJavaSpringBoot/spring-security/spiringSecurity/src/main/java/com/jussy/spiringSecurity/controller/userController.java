package com.jussy.spiringSecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class userController {
    
    @GetMapping
    public String seyhello(){
        return "hello";
    }
}
