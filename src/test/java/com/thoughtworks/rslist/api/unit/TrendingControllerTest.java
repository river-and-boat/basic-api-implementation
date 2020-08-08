package com.thoughtworks.rslist.api.unit;

import com.thoughtworks.rslist.domain.Trade;
import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.BadIndexParamException;
import com.thoughtworks.rslist.repository.TrendingHisRepository;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.TrendingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/8 15:54
 * @Description ***
 **/
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrendingControllerTest {

    @Mock
    private TrendingRepository trendingRepository;

    @Mock
    private TrendingHisRepository trendingHisRepository;

    @Mock
    private UserRepository userRepository;


    private TrendingService trendingService;

    @BeforeEach
    void init() {
        initMocks(this);
        trendingService = new TrendingService(trendingRepository, userRepository, trendingHisRepository);
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
        verify(trendingHisRepository, times(0)).save(any());
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

        when(trendingRepository.existsByPurchaseDegree(anyInt()))
                .thenReturn(false);

        when(trendingRepository.save(any()))
                .thenReturn(TrendingEntity.builder().id(trendingId).keyWord("测试")
                        .purchaseDegree(rant).purchasePrice(amount).totalVotes(10L).trendingName(trendingName).user(new UserEntity()).userId(1).build());
        Trending result = trendingService.purchaseTrendingEvent(trade);

        assertEquals(rant, result.getPurchaseDegree());
        assertEquals(amount, result.getPurchasePrice());
    }
}
