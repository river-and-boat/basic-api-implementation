package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.entity.Trending;
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

@SpringBootTest
class RsControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RsController()).build();
    }

    @Test
    public void testAccessOneTrending() throws Exception {
        Integer accessingRSId = 1;
        mockMvc.perform(get("/trendings/" + accessingRSId))
                .andExpect(jsonPath("$.trendingName", is("热搜事件1")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessTrendingListFromStartToEnd() throws Exception {
        Integer startId = 1;
        Integer endId = 3;
        mockMvc.perform(get("/trendings/range?startId=" + startId + "&endId=" + endId))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].trendingName", is("热搜事件1")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].trendingName", is("热搜事件2")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].trendingName", is("热搜事件3")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testAddOneTrendingEvent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String trendingStr = objectMapper.writeValueAsString(new Trending(6, "热搜事件6", "无分类", null));

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/6"))
                .andExpect(jsonPath("$.trendingName", is("热搜事件6")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    public void testupdateOneTrendingEventWithKeyWord()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Trending trending = new Trending();
        trending.setId(3);
        trending.setKeyWord("国际新闻");
        String updateTrendingStr = objectMapper.writeValueAsString(trending);

        mockMvc.perform(post("/trendings/newTrending")
                .content(updateTrendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + 3))
                .andExpect(jsonPath("$.trendingName", is("热搜事件3")))
                .andExpect(jsonPath("$.keyWord", is("国际新闻")))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    public void testupdateOneTrendingEventWithName()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Trending trending = new Trending();
        trending.setId(3);
        trending.setTrendingName("修改热搜3");
        String updateTrendingStr = objectMapper.writeValueAsString(trending);

        mockMvc.perform(post("/trendings/newTrending")
                .content(updateTrendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + 3))
                .andExpect(jsonPath("$.trendingName", is("修改热搜3")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    public void testupdateOneTrendingEventWithBothFields()
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Trending trending = new Trending();
        trending.setId(3);
        trending.setKeyWord("国际新闻");
        trending.setTrendingName("修改热搜3");
        String updateTrendingStr = objectMapper.writeValueAsString(trending);

        mockMvc.perform(post("/trendings/newTrending")
                .content(updateTrendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + 3))
                .andExpect(jsonPath("$.trendingName", is("修改热搜3")))
                .andExpect(jsonPath("$.keyWord", is("国际新闻")))
                .andExpect(status().isOk());
    }

    @Test
    public void testdeleteTrendingEventById() throws Exception {
        Integer deletingId = 2;

        mockMvc.perform(delete("/trendings/" + deletingId))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/range"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].trendingName", is("热搜事件3")));
    }
}