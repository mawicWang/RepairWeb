package com.duofuen.repair.web;


import com.duofuen.repair.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BasicController {

    @GetMapping("/{path}")
    public String greeting(@PathVariable String path) {
        return path;
    }
}
