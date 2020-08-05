package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.entity.User;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:37
 * @Description ***
 **/
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByUserNameService(Optional<String> userName) {
        if(userName.isPresent()) {
            return userRepository.getUserList().stream()
                    .filter(user -> user.getUserName().equals(userName.get()))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    public Optional<User> addNewUser(Optional<User> newUser) {
        if (newUser.isPresent()) {
            ArrayList<User> userList = userRepository.getUserList();
            boolean userIsPresent = userList.stream()
                    .filter(s -> s.getUserName().equals(newUser.get().getUserName()))
                    .findFirst()
                    .isPresent();
            if (!userIsPresent) {
                // 新增
                userList.add(newUser.get());
            }
        }
        return newUser;
    }

    public List<User> getAllUsers() {
        return userRepository.getUserList();
    }
}
