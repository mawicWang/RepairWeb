package com.duofuen.repair.service;

import com.alibaba.fastjson.JSONObject;
import com.duofuen.repair.domain.*;
import com.duofuen.repair.dto.ChangePasswordDto;
import com.duofuen.repair.dto.UserDto;
import com.duofuen.repair.util.ChuangLanSmsUtil;
import com.duofuen.repair.util.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserInfoRepository userInfoRepository;
    private final AuthoritiesRepository authoritiesRepository;

    @Autowired
    public UserService(UsersRepository usersRepository, BCryptPasswordEncoder encoder,
                       UserInfoRepository userInfoRepository, AuthoritiesRepository authoritiesRepository) {
        this.usersRepository = usersRepository;
        this.encoder = encoder;
        this.userInfoRepository = userInfoRepository;
        this.authoritiesRepository = authoritiesRepository;
    }

    public List<UserDto> getAllUserDtoList() {
        Iterable<Users> listUser = usersRepository.findAll();

        List<UserDto> userDtoList = new ArrayList<>();
        for (Users u : listUser) {
            UserDto dto = new UserDto();
            dto.setUsername(u.getUsername());
            dto.setRole(Const.Role.byAuthority(u.getAuthorities().getAuthority()));
            dto.setName(u.getUserInfo().getName());
            dto.setPhoneNum(u.getUserInfo().getPhoneNum());
            dto.setEnabled(u.getEnabled());
            userDtoList.add(dto);
        }
        return userDtoList;
    }

    public UserDto findUserDtoByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            UserDto dto = new UserDto();
            dto.setRole(Const.Role.ROLE_USER);
            return dto;
        }
        Optional<Users> usersOptional = usersRepository.findById(username);
        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            UserDto dto = new UserDto();
            dto.setUsername(user.getUsername());
            dto.setRole(Const.Role.byAuthority(user.getAuthorities().getAuthority()));
            dto.setName(user.getUserInfo().getName());
            dto.setPhoneNum(user.getUserInfo().getPhoneNum());
            dto.setEnabled(user.getEnabled());
            return dto;
        }
        return null;
    }

    public Users findByUsername(String username) {
        Optional<Users> usersOptional = usersRepository.findById(username);
        return usersOptional.get();
    }

    public void saveUsers(Users users) {
        usersRepository.save(users);
    }

    public boolean resetPassword(String username) {
        Optional<Users> usersOptional = usersRepository.findById(username);
        if (!usersOptional.isPresent()) {
            return false;
        }
        UserInfo userInfo = usersOptional.get().getUserInfo();
        if (userInfo == null || StringUtils.isEmpty(userInfo.getPhoneNum())) {
            return false;
        }

        // 生成密码 8位
        String password = generatePassword();
        String passwordEncoded = encoder.encode(password);

        // 保存密码
        Users user = usersOptional.get();
        user.setPassword(passwordEncoded);
        usersRepository.save(user);
        // 发送短信
        return ChuangLanSmsUtil.sendMsg(userInfo.getPhoneNum(), MessageFormat.format(Const.MSG_REST_PASSWORD, password));
    }

    public JSONObject changePassword(ChangePasswordDto changePasswordDto) {
        JSONObject json = new JSONObject();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Optional<Users> usersOptional = usersRepository.findById(username);
        Assert.isTrue(usersOptional.isPresent(), "WTF current login user is not exists");

        if (!encoder.matches(changePasswordDto.getCurPassword(), usersOptional.get().getPassword())) {
            json.put("success", false);
            json.put("msg", "当前密码输入错误！");
            return json;
        }

        String passwordEncoded = encoder.encode(changePasswordDto.getPassword());

        // 保存密码
        Users user = usersOptional.get();
        user.setPassword(passwordEncoded);
        usersRepository.save(user);
        json.put("success", true);
        json.put("msg", "密码修改成功");
        return json;
    }

    public void saveUserDto(UserDto userDto) {
        Optional<Users> usersOptional = usersRepository.findById(userDto.getUsername());
        if (usersOptional.isPresent()) {
            UserInfo userInfo = usersOptional.get().getUserInfo();
            userInfo.setName(userDto.getName());
            userInfo.setPhoneNum(userDto.getPhoneNum());
            userInfoRepository.save(userInfo);
        } else {
            Users users = new Users();
            users.setUsername(userDto.getUsername());
            users.setPassword(encoder.encode(Const.DEFAULT_PASSWORD));
            users.setEnabled(true);

            Authorities authorities = new Authorities();
            authorities.setUsername(userDto.getUsername());
            authorities.setAuthority(Const.Role.ROLE_USER.getAuthority());

            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(userDto.getUsername());
            userInfo.setName(userDto.getName());
            userInfo.setPhoneNum(userDto.getPhoneNum());

            usersRepository.save(users);
            authoritiesRepository.save(authorities);
            userInfoRepository.save(userInfo);
        }
    }

    public boolean deleteUser(String username) {
        Optional<Users> usersOptional = usersRepository.findById(username);
        if (!usersOptional.isPresent()) {
            return false;
        }

        userInfoRepository.deleteById(username);
        authoritiesRepository.deleteById(username);
        usersRepository.deleteById(username);

        return true;
    }

    private String generatePassword() {
        String[] str = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        Random random = new Random(System.currentTimeMillis());
        StringBuilder verifyBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int rd = random.nextInt(62);
            verifyBuilder.append(str[rd]);
        }
        String password = verifyBuilder.toString();
        return password;
    }

}
