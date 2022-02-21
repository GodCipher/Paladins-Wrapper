package me.skiincraft.api.paladins.storage.impl;

import me.skiincraft.api.paladins.entity.match.Match;

import java.util.ArrayList;
import java.util.List;

public class MatchStorage extends StorageImpl<Match> {

    public MatchStorage(List<Match> item) {
        super(item);
    }

    public MatchStorage() {
        super(new ArrayList<>());
    }

    @Override
    public Match getById(long id) {
        return getAsList().stream().filter((match -> match.getMatchId() == id)).findFirst().orElse(null);
    }
}