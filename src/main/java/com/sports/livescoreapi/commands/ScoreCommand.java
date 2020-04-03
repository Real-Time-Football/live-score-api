package com.sports.livescoreapi.commands;

import com.sports.livescoreapi.TeamSide;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScoreCommand extends Command {

    private TeamSide teamSide;

    public ScoreCommand(String aggregateId, String userId, String version, TeamSide teamSide) {
        super(aggregateId, LocalDateTime.now(), userId, version);
        this.teamSide = teamSide;
    }

}
