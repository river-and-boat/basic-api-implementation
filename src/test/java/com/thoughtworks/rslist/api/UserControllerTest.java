package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.GenderEnum;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @BeforeEach
    void init() {
        userRepository.deleteAll();
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
                .andExpect(jsonPath("$",hasSize(5)))
                .andExpect(jsonPath("$[0]",hasKey("user_name")))
                .andExpect(jsonPath("$[0]",hasKey("user_age")))
                .andExpect(jsonPath("$[0]",hasKey("user_gender")))
                .andExpect(jsonPath("$[0]",hasKey("user_email")))
                .andExpect(jsonPath("$[0]",hasKey("user_phone")))
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

        mockMvc.perform(get("/users/6"))
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

        mockMvc.perform(delete("/users/8"))
                .andExpect(status().isOk());

        List<UserEntity> userEntities = userRepository.findAll();

        assertEquals(userEntities.size(),0);
    }
}