package com.sports.livescoreapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.livescoreapi.commands.EndMatchCommand;
import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import com.sports.livescoreapi.events.GoalScoredEvent;
import com.sports.livescoreapi.events.MatchEndedEvent;
import com.sports.livescoreapi.events.MatchStartedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
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

    @BeforeEach
    void setUp() {
    }

    @Test
    void start_match_successfully() throws Exception {
        StartMatchCommand startMatchCommand = new StartMatchCommand("user-m", "1", LocalDateTime.now(), "PALMEIRAS", "CORINTHIANS");

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
    void get_events_of_match_successfully() throws Exception {
        UUID aggregateId = UUID.randomUUID();

        MatchStartedEvent matchStartedEvent = new MatchStartedEvent(aggregateId, aMatchTime(21, 30), "usr-m", "1", LocalDateTime.now(), "PALMEIRAS", "CORINTHIANS");
        GoalScoredEvent goalScoredEvent1 = new GoalScoredEvent(aggregateId, aMatchTime(21, 40), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent2 = new GoalScoredEvent(aggregateId, aMatchTime(21, 45), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent3 = new GoalScoredEvent(aggregateId, aMatchTime(22, 25), "usr-m", "1", TeamSide.HOME);
        GoalScoredEvent goalScoredEvent4 = new GoalScoredEvent(aggregateId, aMatchTime(22, 40), "usr-m", "1", TeamSide.VISITORS);
        MatchEndedEvent matchEndedEvent = new MatchEndedEvent(aggregateId, aMatchTime(22, 50), "usr-m", "1");

        when(matchQueryHandler.getMatchEvents(aggregateId)).thenReturn(Optional.of(Arrays.asList(
                matchStartedEvent,
                goalScoredEvent1,
                goalScoredEvent2,
                goalScoredEvent3,
                goalScoredEvent4,
                matchEndedEvent
        )));

        mvc.perform(
                get("/match/" + aggregateId.toString() + "/events")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    LocalDateTime aMatchTime(int hour, int min) {
        return LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, min));
    }
}