package com.example.jwtauthorisedlogin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/generalController")
public class GeneralController {

    @GetMapping
    public ResponseEntity<String> welcome(){
        return ResponseEntity.ok("Hey User welcome to the app's secure endpoint");
    }
}
