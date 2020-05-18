package com.sports.livescoreapi.commands;

import com.sports.livescoreapi.domain.TeamSide;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ScoreCommand extends Command {

    private TeamSide teamSide;

    public ScoreCommand(UUID aggregateId, String userId, String version, TeamSide teamSide) {
        super(aggregateId, LocalDateTime.now(), userId, version);
        this.teamSide = teamSide;
    }

}
