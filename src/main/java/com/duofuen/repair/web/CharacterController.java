package com.duofuen.repair.web;

import com.duofuen.repair.domain.Character;
import com.duofuen.repair.domain.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CharacterController {

    private final CharacterRepository characterRepository;

    @Autowired
    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @GetMapping("/listCharacter")
    public String greeting(@RequestParam(name = "r") String roleCode, Model model) {
        Iterable<Character> listCharacter = characterRepository.findAllByRoleCode(roleCode);
        model.addAttribute("listCharacter", listCharacter);
        return "listCharacter";
    }
}
