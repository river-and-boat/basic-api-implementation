package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepository extends PagingAndSortingRepository<VoteEntity, Integer> {
    List<VoteEntity> getVoteEntitiesByVoteTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    List<VoteEntity> getVoteEntitiesByUserIdAndTrendingId(Integer userId, Integer trendingId, Pageable pageable);
}
