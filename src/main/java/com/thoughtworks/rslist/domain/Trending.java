package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/4 20:17
 * @Description ***
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trending {

    private Integer id;

    @ApiModelProperty(value = "热搜名称")
    private String trendingName;

    @ApiModelProperty(value = "热搜关键字")
    private String keyWord;

    private User user;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @Builder.Default
    @ApiModelProperty(value = "获得投票数")
    private Long totalVotes = 0L;

    @Builder.Default
    @ApiModelProperty(value = "如果热搜被购买，该字段为购买金额")
    private Double purchasePrice = 0D;

    @Builder.Default
    @ApiModelProperty(value = "如果热搜被购买，该字段为购买排名")
    private Integer purchaseDegree = 0;

    @JsonIgnore
    @Valid
    public User getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(User user) {
        this.user = user;
    }
}
