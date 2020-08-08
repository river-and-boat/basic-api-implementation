package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/8 18:25
 * @Description ***
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trending_his")
@Builder
public class TrendingEntityHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String trendingName;

    private String keyWord;

    private Long totalVotes;

    private Double purchasePrice;

    private Integer purchaseDegree;

    private Integer userId;

    private LocalDateTime time;
}
