package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Trade;
import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.repository.PurchaseEventRepository;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.tool.ConvertTool;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final TrendingRepository trendingRepository;

    private final UserRepository userRepository;

    private final PurchaseEventRepository purchaseEventRepository;

    public TrendingService(TrendingRepository trendingRepository,
                           UserRepository userRepository,
                           PurchaseEventRepository purchaseEventRepository) {
        this.trendingRepository = trendingRepository;
        this.userRepository = userRepository;
        this.purchaseEventRepository = purchaseEventRepository;
    }

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
                    if (editionObject.getKeyWord() != null) {
                        beforeEdition.setKeyWord(editionObject.getKeyWord());
                    }
                    if (editionObject.getTrendingName() != null) {
                        beforeEdition.setTrendingName(editionObject.getTrendingName());
                    }
                    return ConvertTool
                            .convertTrendingEntityToTrending(trendingRepository.save(beforeEdition));
                }
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

    public Trending purchaseTrendingEvent(Optional<Trade> trade)
            throws BadIndexParamException {
        if (trade.isPresent()) {
            Trade tradeEntity = trade.get();
            // 1. 根据热搜id来找到该热搜
            Optional<TrendingEntity> trending = trendingRepository.findById(tradeEntity.getTrendingId());
            if (!trending.isPresent()) {
                throw new BadIndexParamException("no exist trending in list");
            }
            TrendingEntity presentTrending = trending.get();
            // 3. 查看当前排名是否已被购买
            if (trendingRepository.existsByPurchaseDegree(tradeEntity.getRant())) {
                if (tradeEntity.getAmount() > presentTrending.getPurchasePrice()) {
                    return updatePurchaseEvent(presentTrending, tradeEntity);
                }
                throw new BadIndexParamException("the price is lower than now");
            } else {
                return updatePurchaseEvent(presentTrending, tradeEntity);
            }
        }
        throw new BadIndexParamException("invalid request param");
    }

    private Trending updatePurchaseEvent(TrendingEntity presentTrending, Trade tradeEntity) {
        // 2. 更新现有列表
        presentTrending.setPurchaseDegree(tradeEntity.getRant());
        presentTrending.setPurchasePrice(tradeEntity.getAmount());
        // 3. 加入购买记录

        purchaseEventRepository.save(ConvertTool.convertTrendingToPurchaseEvent(presentTrending, LocalDateTime.now()));
        TrendingEntity result = trendingRepository.save(presentTrending);
        return ConvertTool.convertTrendingEntityToTrending(result);
    }
}
