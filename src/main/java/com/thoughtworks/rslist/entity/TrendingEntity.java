package com.thoughtworks.rslist.entity;

import com.thoughtworks.rslist.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;
}
