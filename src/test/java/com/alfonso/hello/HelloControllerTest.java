package com.alfonso.hello;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web slice: starts the MVC layer only, no server socket is opened.
 */
@WebMvcTest(HelloController.class)
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void rootReturnsDefaultGreeting() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, World!"));
    }

    @Test
    void rootUsesTheNameQueryParameter() throws Exception {
        mockMvc.perform(get("/").param("name", "Alfonso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Alfonso!"));
    }

    @Test
    void healthReportsOk() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    void greetAcceptsAJsonBody() throws Exception {
        mockMvc.perform(post("/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alfonso\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Alfonso!"));
    }

    @Test
    void greetHonoursACustomSalutation() throws Exception {
        mockMvc.perform(post("/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alfonso\",\"greeting\":\"Howdy\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Howdy, Alfonso!"));
    }

    @Test
    void greetRejectsABlankName() throws Exception {
        mockMvc.perform(post("/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"  \"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("validation_failed"))
                .andExpect(jsonPath("$.fields.name").value("must not be blank"));
    }

    @Test
    void greetRejectsAMissingName() throws Exception {
        mockMvc.perform(post("/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value("must not be blank"));
    }

    @Test
    void greetRejectsAnOverlongName() throws Exception {
        String tooLong = "a".repeat(51);
        mockMvc.perform(post("/greet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + tooLong + "\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.name").value("must be at most 50 characters"));
    }
}
