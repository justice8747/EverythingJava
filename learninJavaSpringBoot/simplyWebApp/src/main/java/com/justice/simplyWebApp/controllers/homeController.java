package com.justice.simplyWebApp.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//first you have to identify this to be a controller by using the controller annotation
//ANd for getting the data, well in this case a test there are two way we can do this,
//1, is using the RestController and
//2, is using the ResponseBody annotation

@RestController
public class homeController {
//    this is how we handler request.

    @RequestMapping("/")
//    @ResponseBody
    public String greet(){
        return "hello world";
    }

    @RequestMapping("/about")
//    @ResponseBody
    public String about(){
        return "hope you are good";
    }

}
