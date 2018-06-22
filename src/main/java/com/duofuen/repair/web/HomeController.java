package com.duofuen.repair.web;

import com.duofuen.repair.domain.Character;
import com.duofuen.repair.domain.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CharacterRepository characterRepository;

    @Autowired
    public HomeController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @GetMapping("/listUser")
    public String greeting(Model model) {
        Iterable<Character> listUser = characterRepository.findAll();
        model.addAttribute("listUser", listUser);
        return "listUser";
    }
}
