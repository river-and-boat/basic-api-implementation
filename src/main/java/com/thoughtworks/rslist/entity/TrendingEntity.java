package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/6 15:03
 * @Description ***
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trending")
@Builder
public class TrendingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String trendingName;

    private String keyWord;

    @Builder.Default
    private Long totalVotes = 0L;

    @Builder.Default
    private Double purchasePrice = 0D;

    @Builder.Default
    private Integer purchaseDegree = 0;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "trendingId", fetch = FetchType.LAZY)
    private List<VoteEntity> vote;
}
