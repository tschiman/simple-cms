package com.gettimhired.dave.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    Logger log = LoggerFactory.getLogger(MainController.class);
    @GetMapping("/")
    public String index() {
        log.info("GET / index");
        return "index";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        log.info("GET /gallery gallery");
        return "gallerys";
    }

    @GetMapping("/jobs/{id}")
    public String jobs(@PathVariable String id, Model model) {
        log.info("GET /jobs/{id} gallery jobId={}", id);
        return "jobs";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        log.info("GET /contact contact");
        return "contacts";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        log.info("GET /admin admin");
        return "admins";
    }

    @GetMapping("/login")
    public String login() {
        log.info("GET /login login");
        return "logins";
    }
}
