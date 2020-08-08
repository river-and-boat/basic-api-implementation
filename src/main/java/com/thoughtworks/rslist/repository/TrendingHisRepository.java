package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.TrendingEntityHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrendingHisRepository extends CrudRepository<TrendingEntityHistory, Integer> {
    List<TrendingEntityHistory> findByTrendingName(String trendingName);
}
