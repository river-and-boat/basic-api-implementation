package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.GenderEnum;
import com.thoughtworks.rslist.entity.Trending;
import com.thoughtworks.rslist.entity.User;
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

    public List<User> userList = Stream.of(
            new User(1, "JiangYuzhou", 26, GenderEnum.MALE, "842714673@qq.com", "18883871607")
    ).collect(Collectors.toList());

    @GetMapping("/users/{userName}")
    public User getUserByUserName(@PathVariable("userName") Optional<String> userName) {
        if(userName.isPresent()) {
            return userList.stream()
                    .filter(user -> user.getUserName().equals(userName.get()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @PostMapping("/users/")
    public void addNewUser(@RequestBody @Valid Optional<User> newUser) {
        newUser.ifPresent(t -> {
            boolean userIsPresent = userList.stream()
                    .filter(s -> s.getUserName().equals(t.getUserName()))
                    .findFirst()
                    .isPresent();
            if (!userIsPresent) {
                // 新增
                userList.add(newUser.get());
            }
        });
    }

}
