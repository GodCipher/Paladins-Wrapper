package me.skiincraft.api.paladins.storage.impl;

import me.skiincraft.api.paladins.entity.champions.objects.Cards;

import java.util.ArrayList;

public class CardsStorage extends StorageImpl<Cards> {

    public CardsStorage() {
        super(new ArrayList<>());
    }

    @Override
    public Cards getById(long id) {
        return getAsList().stream().filter((cards) -> cards.getCardsLanguage().getLanguagecode() == id).findFirst().orElse(null);
    }
}
