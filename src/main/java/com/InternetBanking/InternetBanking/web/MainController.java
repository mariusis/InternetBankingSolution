package com.InternetBanking.InternetBanking.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class MainController {

   //Mapping the templates for static pages

    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/")
    public String home() {
        return "index";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    @GetMapping("/offers")
    public String offers() {
        return "offers";
    }


}
