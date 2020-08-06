package com.thoughtworks.rslist.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

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

    private Integer userId;

    private Integer trendingId;

    private LocalTime voteTime;

    private Integer num;
}
