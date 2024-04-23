package com.gypApp_workLoadService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gypApp_workLoadService.dto.TrainerWorkloadRequest;
import com.gypApp_workLoadService.service.TrainerWorkloadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerWorkloadControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private TrainerWorkloadController trainerWorkloadController;

    @Mock
    private TrainerWorkloadService trainerWorkloadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(trainerWorkloadController).build();
    }

    @Test
    void testUpdateWorkload_Success() throws Exception {
        TrainerWorkloadRequest request = TrainerWorkloadRequest.builder().build();

        doNothing().when(trainerWorkloadService).updateWorkload(request);

        mockMvc.perform(post("/updateWorkLoad/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(trainerWorkloadService, times(1)).updateWorkload(request);
    }


}
