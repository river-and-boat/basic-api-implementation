package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.model.Trending;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAccessOneTrending() throws Exception {
        Integer accessingRSId = 1;
        mockMvc.perform(get("/trendings/" + accessingRSId))
                .andExpect(jsonPath("$.trendingName",is("热搜事件1")))
                .andExpect(jsonPath("$.keyWord",is("无分类")))
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
    public void testAddOneTrendingEvent() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String trendingStr = objectMapper.writeValueAsString(new Trending(6,"热搜事件6","无分类"));

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/6"))
                .andExpect(jsonPath("$.trendingName",is("热搜事件6")))
                .andExpect(jsonPath("$.keyWord",is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
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
                .andExpect(jsonPath("$.trendingName",is("热搜事件3")))
                .andExpect(jsonPath("$.keyWord",is("国际新闻")))
                .andExpect(status().isOk());
    }

    @Test
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
                .andExpect(jsonPath("$.trendingName",is("修改热搜3")))
                .andExpect(jsonPath("$.keyWord",is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
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
                .andExpect(jsonPath("$.trendingName",is("修改热搜3")))
                .andExpect(jsonPath("$.keyWord",is("国际新闻")))
                .andExpect(status().isOk());
    }


}