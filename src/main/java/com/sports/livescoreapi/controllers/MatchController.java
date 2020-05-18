package com.sports.livescoreapi.controllers;

import com.sports.livescoreapi.commands.*;
import com.sports.livescoreapi.domain.Match;
import com.sports.livescoreapi.queries.MatchQueryHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
public class MatchController {

    private CommandBus commandBus;
    private MatchQueryHandler matchQueryHandler;

    public MatchController(CommandBus commandBus, MatchQueryHandler matchQueryHandler) {
        this.commandBus = commandBus;
        this.matchQueryHandler = matchQueryHandler;
    }

    @PostMapping("/match/start")
    public ResponseEntity<UUID> startMatch(@Valid @RequestBody StartMatchCommand startMatchCommand) {

        //todo get userId from request
        //todo get version from request

        try {
            commandBus.send(startMatchCommand);
        } catch (ReflectiveOperationException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(startMatchCommand.getAggregateId());
    }

    @PostMapping("/match/score")
    public ResponseEntity score(@Valid @RequestBody ScoreCommand scoreCommand) {

        try {
            commandBus.send(scoreCommand);
        } catch (ReflectiveOperationException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/match/end")
    public ResponseEntity endMatch(@Valid @RequestBody EndMatchCommand endMatchCommand) {
        try {
            commandBus.send(endMatchCommand);
        } catch (ReflectiveOperationException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/match/{aggregateId}")
    public ResponseEntity<Match> getMatch(@PathVariable UUID aggregateId) {
        Optional<Match> match = matchQueryHandler.getMatch(aggregateId);

        if (match.isPresent()) {
            return ResponseEntity.ok(match.get());
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/match/start-period")
    public ResponseEntity startPeriod(@Valid @RequestBody StartPeriodCommand startPeriodCommand) {
        try {
            commandBus.send(startPeriodCommand);
        } catch (ReflectiveOperationException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/match/end-period")
    public ResponseEntity endPeriod(@Valid @RequestBody EndPeriodCommand endPeriodCommand) {
        try {
            commandBus.send(endPeriodCommand);
        } catch (ReflectiveOperationException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
