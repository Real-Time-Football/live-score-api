package com.sports.livescoreapi;

import com.sports.livescoreapi.commands.StartMatchCommand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class MatchController {

    private CommandBus commandBus;

    public MatchController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping("/match/start")
    public ResponseEntity startMatch(@Valid @RequestBody StartMatchInfo startMatchInfo) {

        //todo get userId from request
        String userId = "1";
        //todo get version from request
        String version = "1";

        StartMatchCommand startMatchCommand = new StartMatchCommand(userId, version);

        try {
            commandBus.send(startMatchCommand);
        } catch (ReflectiveOperationException e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/match/{id}/score/{team}")
    public ResponseEntity<MatchAggregate> score() {
        return null;
    }

    @PostMapping("/match/{id}/end")
    public ResponseEntity<MatchAggregate> endMatch() {
        return null;
    }
}
