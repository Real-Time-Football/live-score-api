package com.sports.livescoreapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.livescoreapi.commands.*;
import com.sports.livescoreapi.controllers.MatchController;
import com.sports.livescoreapi.domain.Match;
import com.sports.livescoreapi.domain.TeamSide;
import com.sports.livescoreapi.queries.MatchQueryHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockBean
    private MatchQueryHandler matchQueryHandler;

    private final String USER_ID = "user_x";
    private final String VERSION = "1";
    private final LocalDateTime DATE = LocalDateTime.now();
    private final String HOME = "PALMEIRAS";
    private final String VISITORS = "CORINTHIANS";

    @Test
    void start_match_successfully() throws Exception {
        StartMatchCommand startMatchCommand = new StartMatchCommand(USER_ID, VERSION, DATE, HOME, VISITORS);

        mvc.perform(
                post("/match/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startMatchCommand))
        ).andExpect(status().isOk());
    }

    @Test
    void process_score_goal_successfully() throws Exception {
        ScoreCommand scoreCommand = new ScoreCommand(UUID.randomUUID(), USER_ID, VERSION, TeamSide.HOME);

        mvc.perform(
                post("/match/score")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreCommand))
        ).andExpect(status().isOk());
    }

    @Test
    void end_match_successfully() throws Exception {
        EndMatchCommand endMatchCommand = new EndMatchCommand(UUID.randomUUID(), USER_ID, VERSION);

        mvc.perform(
                post("/match/end")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endMatchCommand))
        ).andExpect(status().isOk());
    }

    @Test
    void get_match_with_current_state_successfully() throws Exception {
        UUID aggregateId = UUID.randomUUID();

        when(matchQueryHandler.getMatch(aggregateId)).thenReturn(Optional.of(new Match(aggregateId)));

        mvc.perform(
                get("/match/" + aggregateId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void return_not_found_when_match_does_not_exist() throws Exception {
        UUID aggregateId = UUID.randomUUID();

        when(matchQueryHandler.getMatch(aggregateId)).thenReturn(Optional.empty());

        mvc.perform(
                get("/match/" + aggregateId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    void start_period_successfully() throws Exception {
        StartPeriodCommand startPeriodCommand = new StartPeriodCommand(UUID.randomUUID(), USER_ID, VERSION);

        mvc.perform(
                post("/match/start-period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startPeriodCommand))
        ).andExpect(status().isOk());
    }

    @Test
    void end_period_successfully() throws Exception {
        EndPeriodCommand endPeriodCommand = new EndPeriodCommand(UUID.randomUUID(), USER_ID, VERSION);

        mvc.perform(
                post("/match/end-period")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(endPeriodCommand))
        ).andExpect(status().isOk());
    }
}