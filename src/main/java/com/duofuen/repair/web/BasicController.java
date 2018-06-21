package com.duofuen.repair.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BasicController {

    @RequestMapping("/{path}")
    public String greeting(@PathVariable String path) {
        return path;
    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/index";
    }
}
