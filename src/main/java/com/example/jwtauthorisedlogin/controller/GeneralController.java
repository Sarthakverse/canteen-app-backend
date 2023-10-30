package com.example.jwtauthorisedlogin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GeneralController {
    @RequestMapping()
    public String generalController(){
        return "hello there!! you are welcome to secure side of app";
    }
}
