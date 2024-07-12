package com.gettimhired.dave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    Logger log = LoggerFactory.getLogger(MainController.class);
    @RequestMapping
    public String index() {
        log.info("GET / index");
        return "index";
    }
}
