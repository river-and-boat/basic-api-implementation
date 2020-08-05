package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.entity.GenderEnum;
import com.thoughtworks.rslist.entity.Trending;
import com.thoughtworks.rslist.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/5 15:14
 * @Description ***
 **/
@SpringBootTest
public class RsWithUserTest {

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
    }


    @Test
    public void testAddTrendingWithNormalUser() throws Exception {
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
