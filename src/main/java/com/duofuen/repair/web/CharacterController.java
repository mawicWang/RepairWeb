package com.duofuen.repair.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.duofuen.repair.domain.*;
import com.duofuen.repair.domain.Character;
import com.duofuen.repair.dto.ZTreeNode;
import com.duofuen.repair.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CharacterController {

    private final CharacterRepository characterRepository;
    private final Address1Repository address1Repository;
    private final Address2Repository address2Repository;
    private final CharaAddr2Repository charaAddr2Repository;

    @Autowired
    public CharacterController(CharacterRepository characterRepository, Address1Repository address1Repository,
                               Address2Repository address2Repository, CharaAddr2Repository charaAddr2Repository) {
        this.characterRepository = characterRepository;
        this.address1Repository = address1Repository;
        this.address2Repository = address2Repository;
        this.charaAddr2Repository = charaAddr2Repository;
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
    @RequestMapping("/saveCharacter")
    @ResponseBody
    public String saveCharacter(@RequestBody Character character) {
        characterRepository.save(character);
        return "success";
    }

    @Transactional
    @RequestMapping("/deleteCharacter")
    @ResponseBody
    public String deleteCharacter(@RequestParam Integer id) {
        characterRepository.deleteById(id);
        return "删除成功！";
    }

    @RequestMapping("/allotArea")
    public String allotArea(Integer characterId, Model model) {
        model.addAttribute("id", characterId);
        return "allotArea";
    }

    @RequestMapping("/getAddressTree")
    @ResponseBody
    public JSONArray getAddressTree(Integer characterId) {
        Optional<Character> character = characterRepository.findById(characterId);
        Assert.isTrue(character.isPresent(), "invalide characterId");

        JSONArray root = new JSONArray();


        List<Address2> associatedAddress2 = address2Repository.findAllByCharacterId(characterId);
        List<Address2> associatedAddress2ByOther = address2Repository.findAllByCharacterIdExcluded(characterId, character.get().getRoleCode());


        Iterable<Address1> listAddress1 = address1Repository.findAll();
        for (Address1 address1 : listAddress1) {
            ZTreeNode node = new ZTreeNode();
            node.setId(address1.getId());
            node.setName(address1.getValue());
            node.setChildren(new ArrayList<>());

            for (Address2 address2 : address1.getListAddress2()) {
                ZTreeNode childNode = new ZTreeNode();
                childNode.setId(address2.getId());
                childNode.setName(address2.getValue());

                if (associatedAddress2.contains(address2)) {
                    childNode.setChecked(true);
                }
                if (associatedAddress2ByOther.contains(address2)) {
                    childNode.setChkDisabled(true);

                    Integer aid = associatedAddress2ByOther.indexOf(address2);
                    Character c = associatedAddress2ByOther.get(aid).getCharacter();
                    if (!StringUtils.isEmpty(c.getUsername())) {
                        if (c.getEnabled()) {
                            childNode.setName(MessageFormat.format("{0}<span class='text-muted'>({1})</span>", childNode.getName(), c.getUsername()));
                        } else {
                            childNode.setName(MessageFormat.format("{0}<span class='text-muted'>({1}<span class='red'> 已禁用</span>)</span>", childNode.getName(), c.getUsername()));

                        }
                    }
                }

                node.getChildren().add(childNode);
            }

            root.add(JSON.toJSON(node));
        }

        return root;
    }

    @Transactional
    @RequestMapping("/saveAddressTree")
    @ResponseBody
    public String saveAddressTree(@RequestBody JSONObject[] changedArr, @RequestParam Integer characterId) {
        for (JSONObject object : changedArr) {

            if ((boolean) object.get("checked")) {
                charaAddr2Repository.save(new CharaAddr2(characterId, (Integer) object.get("id")));
            } else {
                charaAddr2Repository.deleteById(new CharaAddr2PK(characterId, (Integer) object.get("id")));
            }
        }
        return "修改分配区域成功";
    }

    @Transactional
    @RequestMapping("/toggleCharacterEnable")
    @ResponseBody
    public String toggleCharacterEnable(Integer characterId, Boolean enabled) {
        Optional<Character> optionalCharacter = characterRepository.findById(characterId);
        if (!optionalCharacter.isPresent()) {
            return "用户不存在";
        }

        Character character = optionalCharacter.get();
        character.setEnabled(enabled);

        characterRepository.save(character);

        return "修改成功，"
                + (character.getRoleCode().equals(Const.ROLE_CODE_MANAGER) ? "区域经理" : "维修师傅") + " "
                + character.getUsername() + " 已经" + (enabled ? "启用" : "禁用");
    }
}
