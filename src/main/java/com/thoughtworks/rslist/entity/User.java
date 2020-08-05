package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 11:34
 * @Description ***
 *       "user": {
 *         "userName": "xiaowang",
 *         "age": 19,
 *         "gender": "female",
 *         "email": "a@thoughtworks.com",
 *         "phone": 18888888888
 *       }
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    @Size(max = 8)
    @NotNull
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    private Integer age;

    @NotNull
    private GenderEnum genderEnum;

    @Email
    private String email;

    @Pattern(regexp = "^1[3456789]\\d{9}$")
    private String phone;
}

