package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String trendingName;

    private String keyWord;

    private User user;

    private Integer userId;

    @Builder.Default
    private Long totalVotes = 0L;

    @Builder.Default
    private Double purchasePrice = 0D;

    @Builder.Default
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
