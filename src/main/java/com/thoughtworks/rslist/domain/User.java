package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class User {

    private Integer id;

    @Size(max = 8)
    @NotNull
    @JsonProperty("user_name")
    @ApiModelProperty(value = "用户名")
    private String userName;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    @ApiModelProperty(value = "用户年龄")
    private Integer age;

    @NotNull
    @JsonProperty("user_gender")
    @ApiModelProperty(value = "用户性别")
    private GenderEnum genderEnum;

    @Email
    @JsonProperty("user_email")
    @ApiModelProperty(value = "用户邮件")
    private String email;

    @Pattern(regexp = "^1[3456789]\\d{9}$")
    @JsonProperty("user_phone")
    @ApiModelProperty(value = "用户电话")
    private String phone;

    @JsonProperty("vote_num")
    @ApiModelProperty(value = "用户剩余投票数")
    @NotNull
    private Integer voteNum;
}

