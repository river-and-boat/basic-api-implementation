package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.entity.GenderEnum;
import com.thoughtworks.rslist.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }

    @Test
    public void getUserByUserName() throws Exception {
        String userName = "JiangYuzhou";
        mockMvc.perform(get("/users/" + userName))
                .andExpect(jsonPath("$.userName", is(userName)))
                .andExpect(status().isOk());
    }

    @Test
    public void addOneUserWithNormalCondition() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(2, "newUser", 28, GenderEnum.FEMALE, "test@qq.com", "18986457895");
        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/newUser"))
                .andExpect(jsonPath("$.userName", is("newUser")))
                .andExpect(jsonPath("$.email", is("test@qq.com")))
                .andExpect(status().isOk());
    }

    @Test
    public void addOneUserWithNameLargerThan8() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(2, "newUserTest", 28, GenderEnum.FEMALE, "test@qq.com", "18986457895");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithNameNull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(2, null, 28, GenderEnum.FEMALE, "test@qq.com", "18986457895");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithGenderNull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(2, "newUser", 28, null, "test@qq.com", "18986457895");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithAgeNotNull() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(2, "newUser", null, GenderEnum.FEMALE, "test@qq.com", "18986457895");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithAgeLargerThan100() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(2, "newUser", 200, GenderEnum.FEMALE, "test@qq.com", "18986457895");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addOneUserWithAgeLessThan18() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        User newUser = new User(2, "newUser", 12, GenderEnum.FEMALE, "test@qq.com", "18986457895");

        mockMvc.perform(post("/users/")
                .content(objectMapper.writeValueAsString(newUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}