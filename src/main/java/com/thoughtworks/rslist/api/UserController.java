package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.GenderEnum;
import com.thoughtworks.rslist.entity.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 12:19
 * @Description ***
 **/
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userName}")
    public User getUserByUserName(@PathVariable("userName") Optional<String> userName) {
        return userService.getUserByUserNameService(userName);
    }

    @PostMapping("/users/")
    public void addNewUser(@RequestBody @Valid Optional<User> newUser) {
        userService.addNewUser(newUser);
    }

}
