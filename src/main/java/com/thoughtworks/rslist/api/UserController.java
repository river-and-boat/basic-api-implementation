package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.User;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<User> getUserByUserName(@PathVariable("userName") Optional<String> userName) {
        return ResponseEntity.ok(userService.getUserByUserNameService(userName));
    }

    @PostMapping("/users/")
    public ResponseEntity addNewUser(@RequestBody @Valid Optional<User> newUser) {
        Optional<User> user = userService.addNewUser(newUser);
        if (user.isPresent()) {
            Integer id = user.get().getId();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("add",id.toString())
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
