package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.GenderEnum;
import com.thoughtworks.rslist.domain.Trending;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.service.TrendingService;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:14
 * @Description ***
 **/
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TrendingWithUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TrendingRepository trendingRepository;

    @Mock
    private UserService userService;

    @Autowired
    @InjectMocks
    private TrendingService trendingService;

    @Autowired
    private TrendingController trendingController;

    @BeforeEach
    void init() {
        when(userService.addNewUser(any()))
                .thenReturn(1);
    }

    @Test
    public void testAddTrendingWithUserIntegrate() throws Exception {

        String trendingStr = "{\"id\":6, \"trendingName\":\"热搜事件6\"," +
                " \"keyWord\":\"无分类\"," + "\"user\" :{\"id\":2, \"user_name\":\"admin\", " +
                "\"user_age\": 26, \"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/trendings/6"))
                .andExpect(jsonPath("$.trendingName", is("热搜事件6")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testSelectTrendingListWithBadParamRange() throws Exception {
        mockMvc.perform(get("/trendings/range?startId=1&endId=100"))
                .andExpect(jsonPath("$.error",is("invalid request param")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testOneTrendingWithBadParam() throws Exception {
        mockMvc.perform(get("/trendings/100"))
                .andExpect(jsonPath("$.error",is("invalid index")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostBadEventAndUser() throws Exception {
        String trendingStr = "{\"id\":6, \"trendingName\":\"热搜事件6\"," +
                " \"keyWord\":\"无分类\"," + "\"user\" :{\"id\":2, \"user_name\":\"AdminJiangYuzhou\", " +
                "\"user_age\": 14, \"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"1532614723210\"}}";

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error",is("invalid param")))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostBadUser() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, "JiangYuZhouTest", 12, GenderEnum.MALE, "test@qq.com", "18883871607");
        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error", is("invalid user")))
                .andExpect(status().isBadRequest());
    }
}
