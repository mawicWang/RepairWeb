package com.duofuen.repair.web;

import com.duofuen.repair.domain.Users;
import com.duofuen.repair.domain.UsersRepository;
import com.duofuen.repair.dto.UserDto;
import com.duofuen.repair.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/listUser")
    public String listCharacter(Model model) {
        model.addAttribute("listUser", userService.getAllUserDtoList());
        return "listUser";
    }

    @RequestMapping("/detailUser")
    public String detailUser(String username, Model model) {
        model.addAttribute("user", userService.findUserDtoByUsername(username));
        return "detailUser";
    }

    @RequestMapping("/resetPassword")
    @ResponseBody
    public String resetPassword(String username) {
        boolean success = userService.resetPassword(username);
        return success ? "重置密码成功，密码会以短信通知对方。" : "重置密码失败，请检查用户手机号，或联系技术人员。";
    }

    @Transactional
    @RequestMapping("/saveUser")
    @ResponseBody
    public String saveUser(@RequestBody UserDto userDto) {
        userService.saveUserDto(userDto);
        return "success";
    }

    @Transactional
    @RequestMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(String username) {
        boolean deleted = userService.deleteUser(username);
        return deleted ? "删除用户成功！" : "删除用户失败！";
    }

    @Transactional
    @RequestMapping("/toggleUserEnable")
    @ResponseBody
    public String toggleUserEnable(String username, Boolean enabled) {
        Users users = userService.findByUsername(username);
        users.setEnabled(enabled);

        userService.saveUsers(users);

        return "修改成功，用户 " + username + " 已经" + (enabled ? "启用" : "禁用");
    }
}
