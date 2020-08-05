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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TrendingControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TrendingRepository trendingRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    void init() {
        when(trendingRepository.getTrendingList())
                .thenReturn((ArrayList<Trending>) Stream.of(
                        new Trending(1, "热搜事件1", "无分类", new User()),
                        new Trending(2, "热搜事件2", "无分类", new User()),
                        new Trending(3, "热搜事件3", "无分类", new User()),
                        new Trending(4, "热搜事件4", "无分类", new User()),
                        new Trending(5, "热搜事件5", "无分类", new User())
                ).collect(Collectors.toList()));

        when(userService.getUserByUserNameService(any()))
                .thenReturn(new User(1, "JYZ", 26, GenderEnum.MALE, "842714673@qq.com", "18883871607"));

        TrendingService trendingService = new TrendingService(trendingRepository, userService);
        mockMvc = MockMvcBuilders.standaloneSetup(new TrendingController(trendingService)).build();
    }

    @Test
    public void testAccessOneTrending() throws Exception {
        Integer accessingRSId = 1;
        mockMvc.perform(get("/trendings/" + accessingRSId))
                .andExpect(jsonPath("$.trendingName", is("热搜事件1")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
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
    public void testAccessAllTrending() throws Exception {
        mockMvc.perform(get("/trendings/range"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0].trendingName", is("热搜事件1")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].trendingName", is("热搜事件2")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].trendingName", is("热搜事件3")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(jsonPath("$[3].trendingName", is("热搜事件4")))
                .andExpect(jsonPath("$[3].keyWord", is("无分类")))
                .andExpect(jsonPath("$[3]", not(hasKey("user"))))
                .andExpect(jsonPath("$[4].trendingName", is("热搜事件5")))
                .andExpect(jsonPath("$[4].keyWord", is("无分类")))
                .andExpect(jsonPath("$[4]", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddOneTrendingEvent() throws Exception {
        //ObjectMapper objectMapper = new ObjectMapper();
        //String trendingStr = objectMapper.writeValueAsString(new Trending(6, "热搜事件6", "无分类", new User()));

        String trendingStr = "{\"id\":6, \"trendingName\":\"热搜事件6\"," +
                " \"keyWord\":\"无分类\"," + "\"user\" :{\"id\":2, \"userName\":\"Admin\", " +
                "\"genderEnum\":\"MALE\", \"email\":\"hellocq@163.com\", \"phone\":\"15326147230\"}}";

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("add", "6"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/trendings/6"))
                .andExpect(jsonPath("$.trendingName", is("热搜事件6")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
    public void testupdateOneTrendingEventWithKeyWord()
            throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Trending trending = new Trending();
//        trending.setId(3);
//        trending.setKeyWord("国际新闻");
//        trending.setUser(new User());
//        String updateTrendingStr = objectMapper.writeValueAsString(trending);

        String updateTrendingStr = "{\"id\":3, \"trendingName\":\"热搜事件3\"," +
                " \"keyWord\":\"国际新闻\"," + "\"user\" :{\"id\":2, \"userName\":\"Admin\", " +
                "\"genderEnum\":\"MALE\", \"email\":\"hellocq@163.com\", \"phone\":\"15326147230\"}}";

        mockMvc.perform(put("/trendings/exist")
                .content(updateTrendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("update", "3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + 3))
                .andExpect(jsonPath("$.trendingName", is("热搜事件3")))
                .andExpect(jsonPath("$.keyWord", is("国际新闻")))
                .andExpect(status().isOk());
    }

    @Test
    public void testupdateOneTrendingEventWithName()
            throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Trending trending = new Trending();
//        trending.setId(3);
//        trending.setTrendingName("修改热搜3");
//        trending.setUser(new User());
//        String updateTrendingStr = objectMapper.writeValueAsString(trending);

        String updateTrendingStr = "{\"id\":3, \"trendingName\":\"修改热搜3\"," +
                " \"keyWord\":\"无分类\"," + "\"user\" :{\"id\":2, \"userName\":\"Admin\", " +
                "\"genderEnum\":\"MALE\", \"email\":\"hellocq@163.com\", \"phone\":\"15326147230\"}}";

        mockMvc.perform(put("/trendings/exist")
                .content(updateTrendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("update", "3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + 3))
                .andExpect(jsonPath("$.trendingName", is("修改热搜3")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
    public void testupdateOneTrendingEventWithBothFields()
            throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Trending trending = new Trending();
//        trending.setId(3);
//        trending.setKeyWord("国际新闻");
//        trending.setTrendingName("修改热搜3");
//        trending.setUser(new User());
//        String updateTrendingStr = objectMapper.writeValueAsString(trending);

        String updateTrendingStr = "{\"id\":3, \"trendingName\":\"修改热搜3\"," +
                " \"keyWord\":\"国际新闻\"," + "\"user\" :{\"id\":2, \"userName\":\"Admin\", " +
                "\"genderEnum\":\"MALE\", \"email\":\"hellocq@163.com\", \"phone\":\"15326147230\"}}";

        mockMvc.perform(put("/trendings/exist")
                .content(updateTrendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("update", "3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + 3))
                .andExpect(jsonPath("$.trendingName", is("修改热搜3")))
                .andExpect(jsonPath("$.keyWord", is("国际新闻")))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTrendingEventById() throws Exception {
        Integer deletingId = 2;

        mockMvc.perform(delete("/trendings/" + deletingId))
                .andExpect(header().string("delete", "2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/range"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].trendingName", is("热搜事件3")));
    }
}