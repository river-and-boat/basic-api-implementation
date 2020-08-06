package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/6 11:55
 * @Description ***
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    private Integer age;

    @Column(name = "gender_enum")
    private GenderEnum genderEnum;
    private String email;
    private String phone;
}
