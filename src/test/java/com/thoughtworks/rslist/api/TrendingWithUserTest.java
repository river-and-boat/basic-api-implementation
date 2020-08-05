package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.entity.GenderEnum;
import com.thoughtworks.rslist.entity.Trending;
import com.thoughtworks.rslist.entity.User;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.service.TrendingService;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:14
 * @Description ***
 **/
@SpringBootTest
public class TrendingWithUserTest {

    private MockMvc mockMvc;

    @Mock
    private TrendingRepository trendingRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void init() {

        when(trendingRepository.getTrendingList())
                .thenReturn((ArrayList<Trending>) Stream.of(
                        new Trending(1, "热搜事件1", "无分类", null),
                        new Trending(2, "热搜事件2", "无分类", null),
                        new Trending(3, "热搜事件3", "无分类", null),
                        new Trending(4, "热搜事件4", "无分类", null),
                        new Trending(5, "热搜事件5", "无分类", null)
                ).collect(Collectors.toList()));

        when(userService.getUserByUserNameService(any()))
                .thenReturn(new User(1, "JYZ", 26, GenderEnum.MALE, "842714673@qq.com", "18883871607"));

        TrendingService trendingService = new TrendingService(trendingRepository, userService);

        mockMvc = MockMvcBuilders.standaloneSetup(new TrendingController(trendingService)).build();
    }

    @Test
    public void testAddTrendingWithUserIntegrate() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User(2,"Admin",26, GenderEnum.MALE,"hellocq@163.com","15326147230");
        String trendingStr = objectMapper.writeValueAsString(new Trending(6, "热搜事件6", "无分类", user));

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/6"))
                .andExpect(jsonPath("$.trendingName", is("热搜事件6")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$.user.userName", is("Admin")))
                .andExpect(jsonPath("$.user.age", is(26)))
                .andExpect(status().isOk());
    }
}
