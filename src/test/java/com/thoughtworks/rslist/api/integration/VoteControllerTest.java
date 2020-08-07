package com.thoughtworks.rslist.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.GenderEnum;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    public void testVoteTrendingEventWhenVoteNumLessThanOrEqualToRemaining()
            throws Exception {
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

        Vote vote = new Vote();
        vote.setNum(8);
        vote.setUserId(latestUserId);
        //vote.setVoteTime(LocalDateTime.now());

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/trending/" + latestTrendingId + "/vote")
                .content(objectMapper.writeValueAsString(vote))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("add","1"));

        assertEquals(2, userRepository.findById(latestUserId).get().getVoteNum());
        assertEquals(8, trendingRepository.findById(latestTrendingId).get().getTotalVotes());
    }
}