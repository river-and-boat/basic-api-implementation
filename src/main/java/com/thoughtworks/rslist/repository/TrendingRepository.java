package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.TrendingEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:40
 * @Description ***
 **/
@Repository
public interface TrendingRepository extends CrudRepository<TrendingEntity, Integer> {
    List<TrendingEntity> findAll();
    List<TrendingEntity> findByIdBetween(Integer startId, Integer endId);
    Optional<TrendingEntity> findByPurchaseDegree(Integer purchasePrice);
    List<TrendingEntity> findByPurchaseDegreeGreaterThan(Integer degree);
    List<TrendingEntity> findByPurchaseDegreeEqualsOrderByTotalVotesDesc(Integer degree);
}
