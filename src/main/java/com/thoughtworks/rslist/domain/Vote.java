package com.thoughtworks.rslist.domain;

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

    private Integer userId;

    private Integer trendingId;

    private LocalDateTime voteTime;

    private Integer num;
}
