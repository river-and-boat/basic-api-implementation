package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 17:15
 * @Description ***
 **/
@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    public List<UserEntity> findAll();
}
