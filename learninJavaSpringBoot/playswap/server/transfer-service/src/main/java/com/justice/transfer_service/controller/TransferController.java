package com.justice.transfer_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfer")
public class TransferController {
    @GetMapping
    public String sayHello(){
        return "hello from transfer";
    }
}
