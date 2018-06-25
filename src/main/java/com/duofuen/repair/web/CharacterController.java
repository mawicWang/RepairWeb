package com.duofuen.repair.web;

import com.duofuen.repair.domain.Character;
import com.duofuen.repair.domain.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Controller
public class CharacterController {

    private final CharacterRepository characterRepository;

    @Autowired
    public CharacterController(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @RequestMapping("/listCharacter")
    public String listCharacter(@RequestParam(name = "r") String roleCode, Model model) {
        Iterable<Character> listCharacter = characterRepository.findAllByRoleCode(roleCode);
        model.addAttribute("listCharacter", listCharacter);
        model.addAttribute("character", roleCode.equals("00") ? "区域经理" : "维修师傅");
        return "listCharacter";
    }

    @RequestMapping("/detailCharacter")
    public String detailCharacter(@RequestParam(required = false) Integer id, @RequestParam(required = false) String roleCode, Model model) {
        Character character;
        if (id == null) {
            character = new Character();
            character.setRoleCode(roleCode);
        } else {
            character = characterRepository.findById(id).get();
        }
        model.addAttribute("character", character);
        return "detailCharacter";
    }

    @Transactional
    @RequestMapping("saveCharacter")
    public String saveCharacter(@RequestBody Character character) {
        characterRepository.save(character);
        return "listCharacter";
    }

    @Transactional
    @RequestMapping("deleteCharacter")
    public String deleteCharacter(@RequestParam Integer id) {
        characterRepository.deleteById(id);
        return "home";
    }
}
