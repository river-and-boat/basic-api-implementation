package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 11:34
 * @Description ***
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Integer id;

    @Size(max = 8)
    @NotNull
    @JsonProperty("user_name")
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    private Integer age;

    @NotNull
    @JsonProperty("user_gender")
    private GenderEnum genderEnum;

    @Email
    @JsonProperty("user_email")
    private String email;

    @Pattern(regexp = "^1[3456789]\\d{9}$")
    @JsonProperty("user_phone")
    private String phone;

    @JsonProperty("vote_num")
    @NotNull
    private Integer voteNum;
}

