package me.skiincraft.api.paladins.impl.champion;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.champions.objects.ChampionSkin;
import me.skiincraft.api.paladins.internal.requests.APIRequest;
import me.skiincraft.api.paladins.objects.miscellany.Language;
import me.skiincraft.api.paladins.objects.miscellany.Rarity;

public class ChampionSkinTestImpl implements ChampionSkin {

    private long skinId1;
    private long championId;
    private Language language;

    public ChampionSkinTestImpl(long skinId1, long championId, Language language) {
        this.skinId1 = skinId1;
        this.championId =championId;
        this.language = language;
    }

    @Override
    public APIRequest<Champion> getChampion(Language language) {
        return null;
    }

    @Override
    public long getChampionId() {
        return championId;
    }

    @Override
    public String getChampionname() {
        return null;
    }

    @Override
    public Rarity getRarity() {
        return null;
    }

    @Override
    public long getSkinId1() {
        return skinId1;
    }

    @Override
    public long getSkinId2() {
        return 0;
    }

    @Override
    public String getSkinName() {
        return null;
    }

    @Override
    public String getSkinNameEnglish() {
        return null;
    }

    @Override
    public Language getLanguage() {
        return language;
    }
}
