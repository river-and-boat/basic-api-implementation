package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.GenderEnum;
import com.thoughtworks.rslist.domain.Trade;
import com.thoughtworks.rslist.entity.PurchaseEntity;
import com.thoughtworks.rslist.entity.TrendingEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.PurchaseEventRepository;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TrendingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrendingRepository trendingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PurchaseEventRepository purchaseEventRepository;

    @AfterEach
    public void cleanUp() {
        userRepository.deleteAll();
        trendingRepository.deleteAll();
    }

    @Test
    public void testAccessOneTrending() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);

        trendingRepository.save(TrendingEntity.builder().trendingName("vote 1")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());

        Integer accessingRSId = 1;
        mockMvc.perform(get("/trendings/" + accessingRSId))
                .andExpect(jsonPath("$.trendingName", is("vote 1")))
                .andExpect(jsonPath("$.totalVotes", is(50)))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessTrendingListFromStartToEnd() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);

        trendingRepository.save(TrendingEntity.builder().trendingName("vote 1")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 2")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 3")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());

        Integer startId = 1;
        Integer endId = 3;

        mockMvc.perform(get("/trendings/range?startId=" + startId + "&endId=" + endId))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].trendingName", is("vote 1")))
                .andExpect(jsonPath("$[0].totalVotes", is(50)))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].trendingName", is("vote 2")))
                .andExpect(jsonPath("$[1].totalVotes", is(50)))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].trendingName", is("vote 3")))
                .andExpect(jsonPath("$[2].totalVotes", is(50)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessRangedTrending() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);

        trendingRepository.save(TrendingEntity.builder().trendingName("vote 1")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 2")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 3")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 4")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());

        mockMvc.perform(get("/trendings/range?startId=1&endId=5"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].trendingName", is("vote 1")))
                .andExpect(jsonPath("$[0].totalVotes", is(50)))
                .andExpect(jsonPath("$[0]", not(hasKey("user"))))
                .andExpect(jsonPath("$[1].trendingName", is("vote 2")))
                .andExpect(jsonPath("$[1].totalVotes", is(50)))
                .andExpect(jsonPath("$[1]", not(hasKey("user"))))
                .andExpect(jsonPath("$[2].trendingName", is("vote 3")))
                .andExpect(jsonPath("$[2].totalVotes", is(50)))
                .andExpect(jsonPath("$[2]", not(hasKey("user"))))
                .andExpect(jsonPath("$[3].trendingName", is("vote 4")))
                .andExpect(jsonPath("$[3].totalVotes", is(50)))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddOneTrendingEventWithNoLogonUser() throws Exception {

        String trendingStr = "{\"trendingName\":\"热搜事件6\"," +
                " \"keyWord\":\"无分类\"," + "\"user\" :{\"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        assertEquals(trendingRepository.findAll().size(), 0);
    }

    @Test
    public void testAddOneTrendingEventWithLogonUser() throws Exception {

        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("Admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userEntity.setVoteNum(10);

        userRepository.save(userEntity);

        int latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"Trend 6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Integer latestTrendingId = trendingRepository.findAll().get(0).getId();

        mockMvc.perform(get("/trendings/" + latestTrendingId))
                .andExpect(jsonPath("$.trendingName", is("Trend 6")))
                .andExpect(jsonPath("$.keyWord", is("no")))
                .andExpect(jsonPath("$", not(hasKey("user"))))
                .andExpect(status().isOk());

        assertEquals(1, trendingRepository.findAll().size());
    }

    @Test
    public void testAddOneTrendingEvent() throws Exception {
        String trendingStr = "{\"id\":6, \"trendingName\":\"热搜事件6\"," +
                " \"keyWord\":\"无分类\"," + "\"user\" :{\"id\":1, \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("Admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);
        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("add", "1"))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/trendings/1"))
                .andExpect(jsonPath("$.trendingName", is("热搜事件6")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(status().isOk());
    }

    @Test
    public void testPatchTrendingEventWhenTrendingIdDisMatchUserId()
            throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("Admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);
        Integer latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"Trend 6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";
        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Integer latestTrendingId = userRepository.findAll().get(0).getId();

        String requestBody = "{\"trendingName\":\"new Event\"," +
                " \"keyWord\":\"new key word\", \"userId\":2}";

        mockMvc.perform(put("/trendings/exist/" + latestTrendingId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPatchTrendingEventWithNullUserId()
            throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("Admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);
        Integer latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"Trend 6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";
        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Integer latestTrendingId = userRepository.findAll().get(0).getId();

        String requestBody = "{\"trendingName\":\"new Event\"," +
                " \"keyWord\":\"new key word\"}";

        mockMvc.perform(put("/trendings/exist/" + latestTrendingId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPatchTrendingEventWithBothParam()
            throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("Admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);
        Integer latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"Trend 6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";
        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Integer latestTrendingId = trendingRepository.findAll().get(0).getId();

        String requestBody = "{\"trendingName\":\"new Event\"," +
                " \"keyWord\":\"new key word\", \"userId\":" + latestUserId + "}";

        mockMvc.perform(put("/trendings/exist/" + latestTrendingId.toString())
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + latestTrendingId))
                .andExpect(jsonPath("$.trendingName", is("new Event")))
                .andExpect(jsonPath("$.keyWord", is("new key word")))
                .andExpect(status().isOk());
    }

    @Test
    public void testPatchTrendingEventWithNullKeyword()
            throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("Admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);
        Integer latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"Trend 6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";
        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Integer latestTrendingId = trendingRepository.findAll().get(0).getId();

        String requestBody = "{\"trendingName\":\"new Event\"," +
                "\"userId\":" + latestUserId + "}";

        mockMvc.perform(put("/trendings/exist/" + latestTrendingId.toString())
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + latestTrendingId))
                .andExpect(jsonPath("$.trendingName", is("new Event")))
                .andExpect(jsonPath("$.keyWord", is("no")))
                .andExpect(status().isOk());
    }

    @Test
    public void testPatchTrendingEventWithNullTrendingName()
            throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("Admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);
        Integer latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"热搜事件6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32, \"vote_num\":10," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";
        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Integer latestTrendingId = trendingRepository.findAll().get(0).getId();

        String requestBody = "{\"keyWord\":\"new key word\", \"userId\":" + latestUserId + "}";

        mockMvc.perform(put("/trendings/exist/" + latestTrendingId.toString())
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/" + latestTrendingId))
                .andExpect(jsonPath("$.trendingName", is("热搜事件6")))
                .andExpect(jsonPath("$.keyWord", is("new key word")))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTrendingEventById() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);

        trendingRepository.save(TrendingEntity.builder().trendingName("vote 1")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 2")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 3")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 4")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());

        Integer deletingId = 2;

        mockMvc.perform(delete("/trendings/" + deletingId))
                .andExpect(header().string("delete", "2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/trendings/range?startId=1&endId=4"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].trendingName", is("vote 3")));
    }

    @Test
    public void testShowAllTrendingListInOrder() throws Exception {
        String expected =
                "{\"1\":{\"id\":6,\"trendingName\":\"purchase 3\",\"keyWord\":null,\"userId\":null,\"totalVotes\":0,\"purchasePrice\":0.0,\"purchaseDegree\":1}," +
                        "\"2\":{\"id\":2,\"trendingName\":\"vote 5\",\"keyWord\":null,\"userId\":null,\"totalVotes\":100,\"purchasePrice\":0.0,\"purchaseDegree\":0}," +
                        "\"3\":{\"id\":1,\"trendingName\":\"purchase 1\",\"keyWord\":null,\"userId\":null,\"totalVotes\":0,\"purchasePrice\":0.0,\"purchaseDegree\":3}," +
                        "\"4\":{\"id\":5,\"trendingName\":\"vote 2\",\"keyWord\":null,\"userId\":null,\"totalVotes\":80,\"purchasePrice\":0.0,\"purchaseDegree\":0}," +
                        "\"5\":{\"id\":3,\"trendingName\":\"purchase 2\",\"keyWord\":null,\"userId\":null,\"totalVotes\":0,\"purchasePrice\":0.0,\"purchaseDegree\":5}," +
                        "\"6\":{\"id\":4,\"trendingName\":\"vote 1\",\"keyWord\":null,\"userId\":null,\"totalVotes\":50,\"purchasePrice\":0.0,\"purchaseDegree\":0}," +
                        "\"7\":{\"id\":7,\"trendingName\":\"vote 4\",\"keyWord\":null,\"userId\":null,\"totalVotes\":30,\"purchasePrice\":0.0,\"purchaseDegree\":0}," +
                        "\"8\":{\"id\":8,\"trendingName\":\"vote 3\",\"keyWord\":null,\"userId\":null,\"totalVotes\":10,\"purchasePrice\":0.0,\"purchaseDegree\":0}}";

        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);

        trendingRepository.save(TrendingEntity.builder().trendingName("purchase 1")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(3).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 5")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(100L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("purchase 2")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(5).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 1")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(50L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 2")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(80L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("purchase 3")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(1).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 4")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(30L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 3")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(10L).build());

        mockMvc.perform(get("/trendings/order/all"))
                .andExpect(MockMvcResultMatchers.content().json(expected))
                .andExpect(status().isOk());

        List<String> trendingNameArray = Arrays.asList("购买测试3", "投票测试5", "购买测试1", "投票测试2",
                "购买测试2", "投票测试1", "投票测试4", "投票测试3");
    }

    @Test
    public void testPurchaseTrendingInNoExistCase() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);

        trendingRepository.save(TrendingEntity.builder().trendingName("vote 1")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(100L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 2")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(100L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 3")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(100L).build());

        Integer trendingId = 1;
        Integer rant = 1;
        Double amount = 80D;
        Trade trade = new Trade(amount, rant, trendingId);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/trending/purchase")
                .content(objectMapper.writeValueAsString(trade))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        TrendingEntity trendingEntity = trendingRepository.findById(trendingId).get();
        assertEquals(amount, trendingEntity.getPurchasePrice());
        assertEquals(rant, trendingEntity.getPurchaseDegree());

        PurchaseEntity purchaseEntity = purchaseEventRepository.findById(1).get();
        assertEquals(trendingId, purchaseEntity.getTrendingId());
        assertEquals(amount, purchaseEntity.getPurchasePrice());
        assertEquals(rant, purchaseEntity.getPurchaseDegree());
    }

    @Test
    public void testPurchaseTrendingWithOneDifferentExistItem() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");
        userRepository.save(userEntity);

        // 给出的数据中，vote 2已经存在，等级为2，购买价格为70D
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 1")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(100L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 2")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(2).purchasePrice(70D).totalVotes(100L).build());
        trendingRepository.save(TrendingEntity.builder().trendingName("vote 3")
                .user(UserEntity.builder().id(1).build()).purchaseDegree(0).totalVotes(100L).build());

        // 把事件1买到热搜第二位
        Integer trendingId = 1;
        Integer rant = 2;
        Double amount = 80D;
        Trade trade = new Trade(amount, rant, trendingId);

        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(post("/trending/purchase")
                .content(objectMapper.writeValueAsString(trade))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        TrendingEntity trendingEntity = trendingRepository.findById(trendingId).get();
        assertEquals(amount, trendingEntity.getPurchasePrice());
        assertEquals(rant, trendingEntity.getPurchaseDegree());

        int size = trendingRepository.findAll().size();
        assertEquals(2, size);

        PurchaseEntity purchaseEntity = purchaseEventRepository.findById(1).get();
        assertEquals(trendingId, purchaseEntity.getTrendingId());
        assertEquals(amount, purchaseEntity.getPurchasePrice());
        assertEquals(rant, purchaseEntity.getPurchaseDegree());
    }

    public void testPurchaseTrendingWithExceptionAndRollBack() {

    }
}