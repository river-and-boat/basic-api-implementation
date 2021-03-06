package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Trade;
import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.exception.exception_type.BadIndexParamException;
import com.thoughtworks.rslist.exception.exception_type.TrendingShowSortException;
import com.thoughtworks.rslist.repository.PurchaseEventRepository;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.tool.ConvertTool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
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

    private final static Integer DEFAULT_TRENDING_DEGREE = 0;

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
            if (id_start < 0 || id_end < 0 || id_end < id_start) {
                throw new BadIndexParamException("invalid request param");
            } else {
                List<TrendingEntity> resultList = trendingRepository.findByIdBetween(id_start, id_end);
                if (resultList != null) {
                    return resultList
                            .stream().map(s -> ConvertTool.convertTrendingEntityToTrending(s))
                            .collect(Collectors.toList());
                }
                throw new BadIndexParamException("input converting param is null " +
                        "[name: TrendingService.accessTrendingListFromStartToEndService]");
            }
        }
        throw new BadIndexParamException("invalid request param");
    }

    public TreeMap<Integer, Trending> accessAllTrendingListInOrder() throws TrendingShowSortException {
        List<TrendingEntity> purchasedTrendingList = trendingRepository
                .findByPurchaseDegreeGreaterThan(DEFAULT_TRENDING_DEGREE);
        List<TrendingEntity> votedTrendingList = trendingRepository
                .findByPurchaseDegreeEqualsOrderByTotalVotesDesc(DEFAULT_TRENDING_DEGREE);

        if (purchasedTrendingList == null || votedTrendingList == null) {
            throw new TrendingShowSortException("show trending list with unknown error: " +
                    "[name: TrendingService.accessAllTrendingListInOrder]");
        }
        Map<Integer, Trending> hashMap = purchasedTrendingList.stream()
                .collect(Collectors.toMap(TrendingEntity::getPurchaseDegree, k -> ConvertTool.convertTrendingEntityToTrending(k)));
        TreeMap<Integer, Trending> resultMap = new TreeMap<>(hashMap);
        Integer votedTrendingId = 1;
        int votedEventsLength = votedTrendingList.size();
        for (int i = 0; i < votedEventsLength; ) {
            if (!resultMap.containsKey(votedTrendingId)) {
                resultMap.put(votedTrendingId, ConvertTool.convertTrendingEntityToTrending(votedTrendingList.get(i)));
                i += 1;
            }
            votedTrendingId += 1;
        }
        return resultMap;
    }

    public Trending addNewTrending(Optional<Trending> newTrending)
            throws BadIndexParamException {
        if (newTrending.isPresent()) {
            Trending trending = newTrending.get();
            if (trending.getPurchaseDegree() > 0 ||
            trending.getPurchasePrice() > 0 || trending.getTotalVotes() > 0) {
                throw new BadIndexParamException("new trending invalid. name: [TrendingService.addNewTrending]");
            }
            User user = trending.getUser();
            if (user == null || user.getId() == null
                    || !userRepository.existsById(user.getId())) {
                throw new BadIndexParamException("invalid request param");
            }
            TrendingEntity trendingEntity = ConvertTool.convertTrendingToTrendingEntity(trending);
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

    @Transactional(rollbackFor = {Exception.class})
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
            Optional<TrendingEntity> purchaseOptional = trendingRepository
                    .findByPurchaseDegree(tradeEntity.getRant());
            if (purchaseOptional.isPresent()) {
                TrendingEntity purchaseEntity = purchaseOptional.get();
                if (tradeEntity.getAmount() > purchaseEntity.getPurchasePrice()) {
                    return updatePurchaseEvent(presentTrending, tradeEntity, purchaseEntity);
                }
                throw new BadIndexParamException("the price is lower than now");
            } else {
                return updatePurchaseEvent(presentTrending, tradeEntity, null);
            }
        }
        throw new BadIndexParamException("invalid request param");
    }

    private Trending updatePurchaseEvent(TrendingEntity presentTrending, Trade tradeEntity
            , TrendingEntity purchaseEntity) throws BadIndexParamException {
        // 2. 更新现有列表
        presentTrending.setPurchaseDegree(tradeEntity.getRant());
        presentTrending.setPurchasePrice(tradeEntity.getAmount());
        // 3. 加入购买记录
        purchaseEventRepository.save(ConvertTool
                .convertTrendingToPurchaseEvent(presentTrending, LocalDateTime.now()));
        // 4. 如果现有数据存在，则删除现有的购买的热搜
        if (purchaseEntity != null) {
            trendingRepository.deleteById(purchaseEntity.getId());
        }
        TrendingEntity result = trendingRepository.save(presentTrending);
        if (result != null) {
            //throw new BadIndexParamException("Test Transaction");
            return ConvertTool.convertTrendingEntityToTrending(result);
        }
        throw new BadIndexParamException("input converting param is null " +
                "[name: TrendingService.updatePurchaseEvent]");
    }
}
