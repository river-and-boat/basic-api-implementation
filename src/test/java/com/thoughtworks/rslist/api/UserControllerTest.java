package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.entity.GenderEnum;
import com.thoughtworks.rslist.entity.User;
import com.thoughtworks.rslist.repository.UserRepository;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void init() {
        when(userRepository.getUserList())
                .thenReturn((ArrayList<User>)
                        Stream.of(
                                new User(1, "JYZ", 26, GenderEnum.MALE, "842714673@qq.com", "18883871607"),
                                new User(2, "JYZ", 26, GenderEnum.MALE, "842714673@qq.com", "18883871607"),
                                new User(3, "JYZ", 26, GenderEnum.MALE, "842714673@qq.com", "18883871607"),
                                new User(4, "JYZ", 26, GenderEnum.MALE, "842714673@qq.com", "18883871607"),
                                new User(5, "JYZ", 26, GenderEnum.MALE, "842714673@qq.com", "18883871607")
                ).collect(Collectors.toList()));
        UserService userService = new UserService(userRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
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
        User newUser = new User(6, "newUser", 28, GenderEnum.FEMALE, "test@qq.com", "18883871607");
        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/users/newUser"))
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
}