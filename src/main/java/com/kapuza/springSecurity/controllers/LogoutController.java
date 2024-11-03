package com.kapuza.springSecurity.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping()
        public String logout() {
            return "logout";
        }
    }