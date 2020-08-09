package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.Trade;
import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.exception_type.BadIndexParamException;
import com.thoughtworks.rslist.exception.exception_type.TrendingShowSortException;
import com.thoughtworks.rslist.repository.PurchaseEventRepository;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/8 15:54
 * @Description ***
 **/
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrendingServiceTest {

    @Mock
    private TrendingRepository trendingRepository;

    @Mock
    private PurchaseEventRepository purchaseEventRepository;

    @Mock
    private UserRepository userRepository;


    private TrendingService trendingService;

    @BeforeEach
    void init() {
        trendingService = new TrendingService(trendingRepository, userRepository, purchaseEventRepository);
    }

    @Test
    public void testPurchaseTrendingWithNoTrade() {
        Optional<Trade> trade = Optional.ofNullable(null);
        String error = assertThrows(BadIndexParamException.class, () -> {
            trendingService.purchaseTrendingEvent(trade);
        }).getMessage();
        assertEquals(error, "invalid request param");
        verify(trendingRepository, times(0)).findById(anyInt());
    }

    @Test
    public void testPurchaseTrendingWithNoTrending() {
        Integer trendingId = 1;
        Integer rant = 1;
        Double amount = 100D;
        String trendingName = "热搜测试";
        Optional<Trade> trade = Optional.ofNullable(new Trade(100D, 1, trendingId));

        when(trendingRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(null));

        String error = assertThrows(BadIndexParamException.class, () -> {
            trendingService.purchaseTrendingEvent(trade);
        }).getMessage();

        assertEquals(error, "no exist trending in list");
        verify(trendingRepository, times(1)).findById(anyInt());
        verify(purchaseEventRepository, times(0)).save(any());
    }

    @Test
    public void testPurchaseTrendingWithPriceLowerThanNow() throws BadIndexParamException {
        Integer trendingId = 1;
        Integer rant = 1;
        Double amount = 100D;
        String trendingName = "热搜测试";
        Optional<Trade> trade = Optional.ofNullable(new Trade(90D, 1, trendingId));

        when(trendingRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(TrendingEntity.builder().id(trendingId).keyWord("测试")
                        .purchaseDegree(rant).purchasePrice(amount).totalVotes(10L).trendingName(trendingName).user(new UserEntity()).userId(1).build()));

        when(trendingRepository.findByPurchaseDegree(anyInt()))
                .thenReturn(Optional.of(TrendingEntity.builder().purchasePrice(120D).build()));

        String error = assertThrows(BadIndexParamException.class, () -> {
            trendingService.purchaseTrendingEvent(trade);
        }).getMessage();

        assertEquals(error, "the price is lower than now");
        verify(trendingRepository, times(1)).findById(anyInt());
        verify(purchaseEventRepository, times(0)).save(any());
        verify(trendingRepository, times(0)).save(any());
    }

    @Test
    public void testPurchaseTrendingWithDegreeEmpty() throws Exception {
        Integer trendingId = 1;
        Integer rant = 1;
        Double amount = 100D;
        String trendingName = "热搜测试";
        Optional<Trade> trade = Optional.ofNullable(new Trade(100D, 1, trendingId));
        when(trendingRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(TrendingEntity.builder().id(trendingId).keyWord("测试")
                        .purchaseDegree(rant).purchasePrice(0D).totalVotes(10L).trendingName(trendingName).user(new UserEntity()).userId(1).build()));

        when(trendingRepository.findByPurchaseDegree(anyInt()))
                .thenReturn(Optional.ofNullable(null));

        when(trendingRepository.save(any()))
                .thenReturn(TrendingEntity.builder().id(trendingId).keyWord("测试")
                        .purchaseDegree(rant).purchasePrice(amount).totalVotes(10L).trendingName(trendingName).user(new UserEntity()).userId(1).build());
        Trending result = trendingService.purchaseTrendingEvent(trade);

        assertEquals(rant, result.getPurchaseDegree());
        assertEquals(amount, result.getPurchasePrice());
    }

    @Test
    public void testPurchaseTrendingNormal() throws BadIndexParamException {
        Integer trendingId = 1;
        Integer rant = 1;
        Double amount = 80D;
        String trendingName = "热搜测试";
        Optional<Trade> trade = Optional.ofNullable(new Trade(90D, 1, trendingId));

        when(trendingRepository.findById(anyInt()))
                .thenReturn(Optional.ofNullable(TrendingEntity.builder().id(trendingId).keyWord("测试")
                        .purchaseDegree(rant).purchasePrice(amount).totalVotes(10L).trendingName(trendingName)
                        .user(new UserEntity()).userId(1).build()));

        when(trendingRepository.findByPurchaseDegree(anyInt()))
                .thenReturn(Optional.ofNullable(TrendingEntity.builder().id(2).keyWord("待被购买")
                        .purchasePrice(60D).trendingName("待被购买热搜").purchaseDegree(rant).build()));

        TrendingEntity trendingEntity = new TrendingEntity();
        trendingEntity.setUser(new UserEntity());
        when(trendingRepository.save(any()))
                .thenReturn(trendingEntity);

        trendingService.purchaseTrendingEvent(trade);

        verify(trendingRepository, times(1)).findById(anyInt());
        verify(trendingRepository, times(1)).findByPurchaseDegree(anyInt());
        verify(trendingRepository, times(1)).deleteById(anyInt());
        verify(purchaseEventRepository, times(1)).save(any());
        verify(trendingRepository, times(1)).save(any());
    }

    @Test
    public void testShowTrendingListInOrder() throws TrendingShowSortException {
        when(trendingRepository.findByPurchaseDegreeGreaterThan(anyInt()))
                .thenReturn(new ArrayList<TrendingEntity>() {
                    {
                        add(TrendingEntity.builder().trendingName("购买测试1").user(new UserEntity()).purchaseDegree(3).build());
                        add(TrendingEntity.builder().trendingName("购买测试2").user(new UserEntity()).purchaseDegree(5).build());
                        add(TrendingEntity.builder().trendingName("购买测试3").user(new UserEntity()).purchaseDegree(1).build());

                    }
                });

        when(trendingRepository.findByPurchaseDegreeEqualsOrderByTotalVotesDesc(anyInt()))
                .thenReturn(new ArrayList<TrendingEntity>() {
                    {
                        add(TrendingEntity.builder().trendingName("投票测试5").user(new UserEntity()).purchaseDegree(0).totalVotes(100L).build());
                        add(TrendingEntity.builder().trendingName("投票测试2").user(new UserEntity()).purchaseDegree(0).totalVotes(80L).build());
                        add(TrendingEntity.builder().trendingName("投票测试1").user(new UserEntity()).purchaseDegree(0).totalVotes(50L).build());
                        add(TrendingEntity.builder().trendingName("投票测试4").user(new UserEntity()).purchaseDegree(0).totalVotes(30L).build());
                        add(TrendingEntity.builder().trendingName("投票测试3").user(new UserEntity()).purchaseDegree(0).totalVotes(10L).build());
                    }
                });

        TreeMap<Integer, Trending> result = trendingService.accessAllTrendingListInOrder();
        assertEquals(8, result.size());

        List<String> trendingNameArray = Arrays.asList("购买测试3", "投票测试5", "购买测试1", "投票测试2",
                "购买测试2", "投票测试1", "投票测试4", "投票测试3");
        Integer index = 0;
        Iterator<Map.Entry<Integer, Trending>> iterator = result.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Trending> next = iterator.next();
            assertEquals(trendingNameArray.get(index), next.getValue().getTrendingName());
            index += 1;
        }
    }
}
