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
            User user = newTrending.get().getUser();
            if (user == null || user.getId() == null
                    || !userRepository.existsById(user.getId())) {
                throw new BadIndexParamException("invalid request param");
            }
            TrendingEntity trendingEntity = ConvertTool.convertTrendingToTrendingEntity(newTrending.get());
            TrendingEntity saveEntity = trendingRepository.save(trendingEntity);
            return ConvertTool.convertTrendingEntityToTrending(saveEntity);
        }
        throw new BadIndexParamException("invalid request param");
    }

    public Trending updateExistTrending(Optional<Trending> newTrending, Optional<Integer> trendingId)
            throws BadIndexParamException {
        if (trendingId.isPresent()) {
            Optional<TrendingEntity> trendingEntity = trendingRepository.findById(trendingId.get());
            if (trendingEntity.isPresent()) {
                TrendingEntity beforeEdition = trendingEntity.get();
                Trending editionObject = newTrending.get();
                if (beforeEdition.getUserId().compareTo(editionObject.getUserId()) == 0) {
                    beforeEdition.setTrendingName(editionObject.getTrendingName());
                    beforeEdition.setKeyWord(editionObject.getKeyWord());
                    TrendingEntity afterEdition = trendingRepository.save(beforeEdition);
                    return ConvertTool.convertTrendingEntityToTrending(afterEdition);
                }
//                TrendingEntity trendingEntityBeingEdit = trendingEntity.get();
//                if (trending_before.getTrendingName() != null) {
//                    trendingEntityBeingEdit.setTrendingName(trending_before.getTrendingName());
//                }
//                if (trending_before.getKeyWord() != null) {
//                    trendingEntityBeingEdit.setKeyWord(trending_before.getKeyWord());
//                }
//                TrendingEntity saveTrending = trendingRepository.save(trendingEntityBeingEdit);
//                return ConvertTool.convertTrendingEntityToTrending(saveTrending);
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
