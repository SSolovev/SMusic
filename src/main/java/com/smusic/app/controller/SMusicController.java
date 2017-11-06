package com.smusic.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SMusicController {

    private final Logger logger = LoggerFactory.getLogger(SMusicController.class);

    @RequestMapping("/")
    public String root() {
        return "index.html";
    }

    @RequestMapping("/login/main")
    public String login() {
        return "login.html";
    }
//    @RequestMapping("/callback")
//    public String callback(@RequestParam String code) {
//        logger.debug("Received callback code={}", code);
//        return "redirect:index.html";
//    }

}
