package com.sports.livescoreapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.livescoreapi.commands.EndMatchCommand;
import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommandBus commandBus;

    @BeforeEach
    void setUp() {
    }

    @Test
    void start_match_successfully() throws Exception {
        StartMatchCommand startMatchCommand = new StartMatchCommand("user-m", "1");

        mvc.perform(
                post("/match/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startMatchCommand))
        ).andExpect(status().isOk());
    }

    @Test
    void process_score_goal_successfully() throws Exception {
        ScoreCommand scoreCommand = new ScoreCommand(UUID.randomUUID(), "usr-m", "1", TeamSide.HOME);

        mvc.perform(
                post("/match/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreCommand))
        ).andExpect(status().isOk());
    }

    @Test
    void end_match_successfully() throws Exception {
        EndMatchCommand endMatchCommand = new EndMatchCommand(UUID.randomUUID(), "usr-m", "1");

        mvc.perform(
                post("/match/end")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endMatchCommand))
        ).andExpect(status().isOk());
    }
}