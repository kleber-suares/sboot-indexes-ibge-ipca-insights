package com.kls.references.indexes.ibge.ipca.insights.api.handler;


import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiResponseException;
import com.kls.references.indexes.ibge.ipca.insights.infrastructure.exception.ExternalApiTimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ApiExceptionHandlerTest {

    private MockMvc mockMvc;

    @RestController
    static class DummyController {

        @GetMapping("/throw-generic")
        public void throw_GenericException() {
            throw new RuntimeException("Unexpected error");
        }

        @GetMapping("/throw-external")
        public void throw_ExternalApiResponseException() {
            throw new ExternalApiResponseException("No data returned from external service");
        }

        @GetMapping("/throw-timeout")
        public void throw_ExternalApiTimeoutException() {
            throw new ExternalApiTimeoutException(
                "The timeout period elapsed prior to completion of the operation ",
                new RuntimeException("Request timeout")
            );
        }
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new DummyController())
            .setControllerAdvice(new ApiExceptionHandler())
            .build();
    }

    @Test
    void shouldReturn500_When_GenericException_IsThrown() throws Exception {
        mockMvc.perform(get("/throw-generic"))
            .andExpect(status().isInternalServerError())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message",
                is("An internal error ocurred while processing the request.")));
    }

    @Test
    void shouldReturn502_When_ExternalApiException_IsThrown() throws Exception {
        mockMvc.perform(get("/throw-external"))
            .andExpect(status().isBadGateway())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message",
                is("The external service didn't return a valid response.")));
    }

    @Test
    void shouldReturn504_When_ExternalApiTimeoutException_IsThrown() throws Exception {
        mockMvc.perform(get("/throw-timeout"))
            .andExpect(status().isGatewayTimeout())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message",
                is("The request was aborted: The external service took too long to respond and the operation has timed out.")));
    }

}