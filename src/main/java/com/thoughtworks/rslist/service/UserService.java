package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.tool.ConvertTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:37
 * @Description ***
 **/
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final static Integer NAN_USER = -1;

    public User getUserByUserNameService(Optional<String> userName) {
//        if(userName.isPresent()) {
//            return userRepository.getUserList().stream()
//                    .filter(user -> user.getUserName().equals(userName.get()))
//                    .findFirst()
//                    .orElse(null);
//        }
        return null;
    }

    public Integer addNewUser(Optional<User> newUser) {
        if (newUser.isPresent()) {
            User convertingUser = newUser.get();
            // 类型转换
            UserEntity userEntity = ConvertTool.convertUserToUserEntity(convertingUser);
            UserEntity saveUser = userRepository.save(userEntity);
            if (saveUser != null) {
                return saveUser.getId();
            }
        }
        return NAN_USER;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(s -> ConvertTool.convertUserEntityToUser(s))
                .collect(Collectors.toList());
    }
}
