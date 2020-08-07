package com.thoughtworks.rslist.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.GenderEnum;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrendingRepository trendingRepository;

    @Autowired
    private VoteRepository voteRepository;

    @AfterEach
    void cleanUp() {
        //userRepository.deleteAll();
    }

    @Test
    public void testVoteTrendingEventWhenVoteNumLargerThanRemaining()
            throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("JYZ");
        userEntity.setPhone("18883871607");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setEmail("hello@cq.com");
        userEntity.setAge(26);
        userEntity.setVoteNum(10);
        userRepository.save(userEntity);

        Integer latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"Trend 6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10, " +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";
        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Integer latestTrendingId = trendingRepository.findAll().get(0).getId();

        String voteTime = "2020-08-07 21:51:22";

        Vote vote = new Vote();
        vote.setNum(15);
        vote.setTrendingId(latestTrendingId);
        vote.setUserId(latestUserId);
        //vote.setVoteTime(LocalDateTime.parse(voteTime, df));

        ObjectMapper objectMapper = new ObjectMapper();


        mockMvc.perform(post("/trending/" + latestTrendingId + "/vote")
                .content(objectMapper.writeValueAsString(vote))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // 需要再测试时间格式的Json字符串的获取
    @Test
    public void testVoteTrendingEventWhenVoteNumLessThanOrEqualToRemaining()
            throws Exception {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("JYZ");
        userEntity.setPhone("18883871607");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setEmail("hello@cq.com");
        userEntity.setAge(26);
        userEntity.setVoteNum(10);
        userRepository.save(userEntity);

        Integer latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"Trend 6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10, " +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";
        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Integer latestTrendingId = trendingRepository.findAll().get(0).getId();

        String voteTime = "2020-08-07 21:51:22";
        Vote vote = new Vote();
        vote.setNum(8);
        vote.setUserId(latestUserId);
        vote.setVoteTime(LocalDateTime.parse(voteTime, df));

        ObjectMapper objectMapper = new ObjectMapper();

        String s = objectMapper.writeValueAsString(vote);

        mockMvc.perform(post("/trending/" + latestTrendingId + "/vote")
                .content(objectMapper.writeValueAsString(vote))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("add", "1"));

        assertEquals(2, userRepository.findById(latestUserId).get().getVoteNum());
        assertEquals(8, trendingRepository.findById(latestTrendingId).get().getTotalVotes());
    }

    // 测试事务是否回滚
    public void testTransaction() {

    }

    @Test
    public void getVoteEventBetweenTime() throws Exception {
        Integer countDatas = 10;

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String voteTime = "";

        for (int i = 1; i < countDatas; i++) {
            UserEntity userEntity = UserEntity.builder()
                    .phone("18883871607").email("842714673@qq.com").genderEnum(GenderEnum.MALE)
                    .age(26).userName("JYZ").voteNum(i).build();
            userRepository.save(userEntity);
        }

        for (int i = 1; i < countDatas; i++) {
            TrendingEntity trendingEntity = TrendingEntity.builder()
                    .trendingName("Test").id(i).keyWord("Test").purchaseDegree(i)
                    .purchasePrice(2D).totalVotes(10L).userId(i).build();
            trendingRepository.save(trendingEntity);
        }

        for (int i = 1; i < countDatas; i++) {
            voteTime = "200" + i + "-08-07 21:51:22";
            VoteEntity voteEntity = VoteEntity.builder()
                    .num(i).trendingId(i).userId(i)
                    .voteTime(LocalDateTime.parse(voteTime, df)).build();
            voteRepository.save(voteEntity);
        }


        String startTime = "2004-01-01 00:00:00";
        String endTime = "2007-08-08 00:00:00";

        mockMvc.perform(get("/trendings").param("startTime", startTime)
                .param("endTime", endTime))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].num", is(4)))
                .andExpect(status().isOk());
    }
}