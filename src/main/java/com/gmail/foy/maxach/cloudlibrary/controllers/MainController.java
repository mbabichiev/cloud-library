package com.gmail.foy.maxach.cloudlibrary.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


@Controller
public class MainController {

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public String home(Model model) {
        return "home";
    }
}
