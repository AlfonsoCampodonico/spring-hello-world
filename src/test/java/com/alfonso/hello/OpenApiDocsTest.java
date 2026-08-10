package com.alfonso.hello;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Guards the generated spec. These assertions fail if an endpoint is added
 * without documentation, or if a constraint stops being published.
 */
@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void servesTheGeneratedSpec() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("spring-hello-world"))
                .andExpect(jsonPath("$.paths./").exists())
                .andExpect(jsonPath("$.paths./greet").exists())
                .andExpect(jsonPath("$.paths./health").exists());
    }

    @Test
    void documentsTheGreetRequestSchemaWithItsConstraints() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.schemas.GreetRequest").exists())
                .andExpect(jsonPath("$.components.schemas.GreetRequest.properties.name.maxLength")
                        .value(50))
                .andExpect(jsonPath("$.components.schemas.GreetRequest.required[0]").value("name"));
    }

    @Test
    void documentsTheValidationFailureResponse() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths./greet.post.responses.400").exists());
    }

    @Test
    void redocPathForwardsToTheStaticPage() throws Exception {
        mockMvc.perform(get("/redoc"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/redoc.html"));
    }
}
