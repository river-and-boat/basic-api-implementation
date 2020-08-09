package com.thoughtworks.rslist.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/8 15:49
 * @Description ***
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    @ApiModelProperty(value = "购买热搜金额")
    public Double amount;

    @ApiModelProperty(value = "购买热搜排名")
    public Integer rant;

    @ApiModelProperty(value = "购买热搜id")
    public Integer trendingId;
}
