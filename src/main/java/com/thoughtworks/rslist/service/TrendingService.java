package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.exception.IndexOutException;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.tool.ConvertTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:36
 * @Description ***
 **/
@Service
public class TrendingService {

    @Autowired
    private TrendingRepository trendingRepository;

    @Autowired
    private UserRepository userRepository;

    private final static Integer NAN_TRENDING = -1;

    public Trending accessOneTrendingByIdService(Optional<Integer> id)
            throws Exception {
        if (id.isPresent()) {
            Optional<TrendingEntity> trendingEntity = trendingRepository.findById(id.get());
            if (trendingEntity.isPresent()) {
                return ConvertTool.convertTrendingEntityToTrending(trendingEntity.get());
            }
        }
        return null;
    }

    public List<Trending> accessTrendingListFromStartToEndService(Optional<Integer> startId,
                                                                  Optional<Integer> endId)
            throws BadIndexParamException {
        if (startId.isPresent() && endId.isPresent()) {
            Integer id_start = startId.get();
            Integer id_end = endId.get();
            if (id_start < 0 || id_end < 0 || id_end > id_start) {
                throw new BadIndexParamException("invalid request param");
            } else {
                return trendingRepository.findByIdBetween(id_start, id_end)
                        .stream().map(s -> ConvertTool.convertTrendingEntityToTrending(s))
                        .collect(Collectors.toList());
            }
        }
        throw new BadIndexParamException("invalid request param");
    }

    public Trending addNewTrending(Optional<Trending> newTrending)
            throws BadIndexParamException {
        if (newTrending.isPresent()) {
            TrendingEntity trendingEntity = ConvertTool.convertTrendingToTrendingEntity(newTrending.get());
            UserEntity userEntity = trendingEntity.getUser();
            if(userEntity != null && !userRepository.existsById(userEntity.getId())) {
                userRepository.save(trendingEntity.getUser());
            }
            TrendingEntity saveEntity = trendingRepository.save(trendingEntity);
            return ConvertTool.convertTrendingEntityToTrending(saveEntity);
        }
        throw new BadIndexParamException("invalid request param");
    }

    public Trending updateExistTrending(Optional<Trending> newTrending)
            throws BadIndexParamException {
        if (newTrending.isPresent()) {
            Trending trending_before = newTrending.get();
            Optional<TrendingEntity> trendingEntity = trendingRepository.findById(trending_before.getId());
            if (trendingEntity.isPresent()) {
                TrendingEntity trendingEntityBeingEdit = trendingEntity.get();
                if (trending_before.getTrendingName() != null) {
                    trendingEntityBeingEdit.setTrendingName(trending_before.getTrendingName());
                }
                if (trending_before.getKeyWord() != null) {
                    trendingEntityBeingEdit.setKeyWord(trending_before.getKeyWord());
                }
                TrendingEntity saveTrending = trendingRepository.save(trendingEntityBeingEdit);
                return ConvertTool.convertTrendingEntityToTrending(saveTrending);
            }
        }
        throw new BadIndexParamException("invalid request param");
    }

    public Integer deleteTrendingById(Optional<Integer> id)
            throws BadIndexParamException {
        if (id.isPresent()) {
            Integer index = id.get();
            trendingRepository.deleteById(index);
            return index;
        }
        throw new BadIndexParamException("invalid request param");
    }
}
