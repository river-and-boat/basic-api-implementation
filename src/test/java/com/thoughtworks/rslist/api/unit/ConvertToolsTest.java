package com.thoughtworks.rslist.api.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Auto Jiang Yuzhou
 * @Date 2020/8/8 13:43
 * @Description ***
 **/

public class ConvertToolsTest {

    @Autowired
    private MockMvc mockMvc;

    public void testConvertToolsMethodWithNullInput() throws Exception {
        mockMvc.perform(post("/time/voteEvents"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", is("The datetime is bad format")));
    }
}
