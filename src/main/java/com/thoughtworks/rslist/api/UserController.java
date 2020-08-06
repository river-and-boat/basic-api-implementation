package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserByUserName(@PathVariable("id") Optional<Integer> id)
            throws BadIndexParamException {
        return ResponseEntity.ok(userService.getUserByUserId(id));
    }

    @PostMapping("/users/")
    public ResponseEntity addNewUser(@RequestBody @Valid Optional<User> newUser) {
        Integer userId = userService.addNewUser(newUser);
        if (userId > 0) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("add",userId.toString())
                    .body(null);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/users/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
