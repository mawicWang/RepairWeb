package com.duofuen.repair.web;

import com.duofuen.repair.domain.User;
import com.duofuen.repair.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    @Autowired
    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/tb")
    public String greeting(Model model) {
        Iterable<User> listUser = userRepository.findAll();
        model.addAttribute("listUser", listUser);
        return "tb";
    }
}
