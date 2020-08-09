package com.thoughtworks.rslist.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/6 23:51
 * @Description ***
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "热搜id")
    private Integer trendingId;

    @ApiModelProperty(value = "投票时间")
    private LocalDateTime voteTime;

    @ApiModelProperty(value = "投票数")
    private Integer num;
}
