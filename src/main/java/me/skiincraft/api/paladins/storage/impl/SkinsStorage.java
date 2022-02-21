package me.skiincraft.api.paladins.storage.impl;

import me.skiincraft.api.paladins.entity.champions.objects.Skins;

import java.util.ArrayList;

public class SkinsStorage extends StorageImpl<Skins> {

    public SkinsStorage() {
        super(new ArrayList<>());
    }

    @Override
    public Skins getById(long id) {
        return getAsList().stream().filter((skins) -> skins.getChampionId() == id).findFirst().orElse(null);
    }
}