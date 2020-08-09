package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.exception.MysqlOperatingException;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.tool.ConvertTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    private final UserRepository userRepository;

    private final static Integer NAN_USER = -1;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByUserId(Optional<Integer> id)
            throws BadIndexParamException {
        if(id.isPresent()) {
            Optional<UserEntity> userResult = userRepository.findById(id.get());
            if (userResult.isPresent()) {
                return ConvertTool.convertUserEntityToUser(userResult.get()) ;
            }
        }
        throw new BadIndexParamException("invalid request param");
    }

    public Integer addNewUser(Optional<User> newUser) throws BadIndexParamException {
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

    public List<User> getAllUsers() throws BadIndexParamException {
        List<UserEntity> resultList = userRepository.findAll();
        if (resultList != null) {
            return resultList.stream()
                    .map(s ->  ConvertTool.convertUserEntityToUser(s))
                    .collect(Collectors.toList());
        }
        throw new BadIndexParamException("input converting param is null " +
                "[name: UserService.getAllUsers]");
    }

    public Integer deleteUserById(Optional<Integer> id)
            throws MysqlOperatingException, BadIndexParamException {
        try {
            if (id.isPresent()) {
                userRepository.deleteById(id.get());
                return id.get();
            }
        } catch (Exception ex) {
            throw new MysqlOperatingException("mysql deleting error");
        }
        throw new BadIndexParamException("invalid request param");
    }
}
