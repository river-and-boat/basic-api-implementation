package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.PurchaseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseEventRepository extends CrudRepository<PurchaseEntity, Integer> {
    List<PurchaseEntity> findByTrendingName(String trendingName);
}
