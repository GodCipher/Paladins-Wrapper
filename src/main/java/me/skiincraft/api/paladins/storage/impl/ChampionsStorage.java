package me.skiincraft.api.paladins.storage.impl;

import me.skiincraft.api.paladins.entity.champions.Champions;

import java.util.ArrayList;
import java.util.List;

public class ChampionsStorage extends StorageImpl<Champions> {

    public ChampionsStorage(List<Champions> item) {
        super(item);
    }

    public ChampionsStorage() {
        super(new ArrayList<>());
    }

    @Override
    public Champions getById(long id) {
        return getAsList().stream().filter((champions -> champions.getLanguage().getLanguagecode() == id)).findFirst().orElse(null);
    }
}


