package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.EndMatchCommand;
import com.sports.livescoreapi.commands.ScoreCommand;
import com.sports.livescoreapi.commands.StartMatchCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.UUID;

@Controller
public class MatchController {

    private CommandBus commandBus;

    public MatchController(CommandBus commandBus) {
        this.commandBus = commandBus;
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
    public ResponseEntity<Match> endMatch(@Valid @RequestBody EndMatchCommand endMatchCommand) {
        try {
            commandBus.send(endMatchCommand);
        } catch (ReflectiveOperationException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
