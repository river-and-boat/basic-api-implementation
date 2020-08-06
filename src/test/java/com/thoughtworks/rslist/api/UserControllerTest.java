package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.GenderEnum;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.TrendingRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private UserService userService;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrendingRepository trendingRepository;

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        trendingRepository.deleteAll();
    }

    @Test
    public void getUserByUserName() throws Exception {
        String userName = "JYZ";
        mockMvc.perform(get("/users/" + userName))
                .andExpect(jsonPath("$.user_name", is(userName)))
                .andExpect(status().isOk());
    }

    @Test
    public void addOneUserWithNormalCondition() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(1, "newUser", 28, GenderEnum.FEMALE, "test@qq.com", "18883871607");
        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/all"))
                .andExpect(jsonPath("$.user_name", is("newUser")))
                .andExpect(jsonPath("$.user_email", is("test@qq.com")))
                .andExpect(status().isOk());
    }

    @Test
    public void addOneExistUserWithNormalCondition() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(1, "JYZ", 26, GenderEnum.MALE, "test@qq.com", "18883871607");
        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/JYZ"))
                .andExpect(jsonPath("$.user_name", is("JYZ")))
                .andExpect(jsonPath("$.user_email", is("842714673@qq.com")))
                .andExpect(status().isOk());
    }

    @Test
    public void addOneUserWithNameLargerThan8() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, "newUserTest", 28, GenderEnum.FEMALE, "test@qq.com", "18883871607");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithNameNull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, null, 28, GenderEnum.FEMALE, "test@qq.com", "18883871607");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithGenderNull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, "newUser", 28, null, "test@qq.com", "18883871607");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithAgeNotNull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, "newUser", null, GenderEnum.FEMALE, "test@qq.com", "18883871607");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithAgeLargerThan100() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, "newUser", 200, GenderEnum.FEMALE, "test@qq.com", "18883871607");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithAgeLessThan18() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, "newUser", 12, GenderEnum.FEMALE, "test@qq.com", "18883871607");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithInvalidEmail() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, "newUser", 26, GenderEnum.FEMALE, "test_qq.com", "18883871607");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithInvalidPhoneNumber() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(6, "newUser", 26, GenderEnum.FEMALE, "test@qq.com", "389864578952");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllUsers() throws Exception {
        mockMvc.perform(get("/users/all"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0]", hasKey("user_name")))
                .andExpect(jsonPath("$[0]", hasKey("user_age")))
                .andExpect(jsonPath("$[0]", hasKey("user_gender")))
                .andExpect(jsonPath("$[0]", hasKey("user_email")))
                .andExpect(jsonPath("$[0]", hasKey("user_phone")))
                .andExpect(status().isOk());
    }

    @Test
    public void testSelectUserFromMySqlById() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("JYZ");
        userEntity.setPhone("18883871607");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setEmail("hello@cq.com");
        userEntity.setAge(26);
        userRepository.save(userEntity);

        int idToDelete = userRepository.findAll().get(0).getId();

        mockMvc.perform(get("/users/" + idToDelete))
                .andExpect(jsonPath("$.user_name", is("JYZ")))
                .andExpect(jsonPath("$.user_age", is(26)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserFromMySqlById() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("JYZ");
        userEntity.setPhone("18883871607");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setEmail("hello@cq.com");
        userEntity.setAge(26);
        userRepository.save(userEntity);

        int idToDelete = userRepository.findAll().get(0).getId();

        mockMvc.perform(delete("/users/" + idToDelete))
                .andExpect(status().isOk());

        List<UserEntity> userEntities = userRepository.findAll();

        assertEquals(userEntities.size(), 0);
    }

    @Test
    public void testCascadingDeletionUserAndTrending() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setAge(32);
        userEntity.setUserName("Admin");
        userEntity.setEmail("hellocq@163.com");
        userEntity.setGenderEnum(GenderEnum.MALE);
        userEntity.setPhone("15326147230");

        userRepository.save(userEntity);

        Integer latestUserId = userRepository.findAll().get(0).getId();

        String trendingStr = "{\"trendingName\":\"Trend 6\", " +
                "\"keyWord\":\"no\"," + "\"user\" :{\"id\":" + latestUserId + ", \"user_name\":\"Admin\", \"user_age\": 32," +
                "\"user_gender\":\"MALE\", \"user_email\":\"hellocq@163.com\", \"user_phone\":\"15326147230\"}}";

        mockMvc.perform(post("/trendings/newTrending")
                .content(trendingStr)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/users/" + latestUserId))
                .andExpect(header().string("delete", latestUserId.toString()))
                .andExpect(status().isOk());

        assertEquals(0, trendingRepository.findAll().size());
    }
}