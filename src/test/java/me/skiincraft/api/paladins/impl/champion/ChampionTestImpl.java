package me.skiincraft.api.paladins.impl.champion;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.champions.objects.Cards;
import me.skiincraft.api.paladins.entity.champions.objects.Skins;
import me.skiincraft.api.paladins.internal.requests.APIRequest;
import me.skiincraft.api.paladins.objects.champion.Ability;
import me.skiincraft.api.paladins.objects.miscellany.Language;

import java.util.List;

public class ChampionTestImpl implements Champion {

    private long id;
    private String name;

    public ChampionTestImpl(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getRoleString() {
        return null;
    }

    @Override
    public String getLore() {
        return null;
    }

    @Override
    public int getHealth() {
        return 0;
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public List<Ability> getAbilities() {
        return null;
    }

    @Override
    public boolean isLatestChampion() {
        return false;
    }

    @Override
    public boolean inFreeRotation() {
        return false;
    }

    @Override
    public boolean inFreeWeeklyRotation() {
        return false;
    }

    @Override
    public APIRequest<Cards> getCards() {
        return null;
    }

    @Override
    public APIRequest<Skins> getSkins() {
        return null;
    }

    @Override
    public Language getLanguage() {
        return null;
    }
}
