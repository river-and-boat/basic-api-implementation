package com.thoughtworks.rslist.repository;
import com.thoughtworks.rslist.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 17:15
 * @Description ***
 **/
@Repository
public class UserRepository {
    public ArrayList<User> getUserList() {
        return new ArrayList<>();
    }
}
