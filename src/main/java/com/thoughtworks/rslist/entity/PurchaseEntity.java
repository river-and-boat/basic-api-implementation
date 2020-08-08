package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/8 18:25
 * @Description ***
 **/
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event_purchase")
@Builder
public class PurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String trendingName;

    private Double purchasePrice;

    private Integer purchaseDegree;

    private LocalDateTime time;

    @Column(name = "trending_Id")
    private Integer trendingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trending_Id", updatable = false, insertable = false)
    private TrendingEntity trendingEntity;
}
