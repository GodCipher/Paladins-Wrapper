package me.skiincraft.api.paladins.test;

import me.skiincraft.api.paladins.entity.champions.objects.ChampionSkin;
import me.skiincraft.api.paladins.entity.champions.objects.Skins;
import me.skiincraft.api.paladins.impl.champion.ChampionSkinTestImpl;
import me.skiincraft.api.paladins.storage.impl.PaladinsStorageImpl;
import me.skiincraft.api.paladins.objects.miscellany.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class SkinsStorageTest {

    @Test
    @DisplayName("Add skins to Storage")
    @Order(1)
    void Add_Skins_To_Storage() {
        PaladinsStorageImpl storage = (PaladinsStorageImpl) PaladinsStorageImpl.getInstance();
        ChampionSkin skin = new ChampionSkinTestImpl(1, 1, Language.Portuguese);
        Skins skins = new Skins(Collections.singletonList(skin), 1, Language.Portuguese);

        storage.store(skins);

        storage.getChampionsStorage().size();
        assert storage.skinIsStored(1, Language.Portuguese);
    }

    @Test
    @DisplayName("Add existing skins to storage")
    @Order(2)
    void Add_Existing_Skins_To_Storage() {
        PaladinsStorageImpl storage = (PaladinsStorageImpl) PaladinsStorageImpl.getInstance();
        ChampionSkin skin = new ChampionSkinTestImpl(1, 1, Language.Portuguese);
        Skins instance1 = new Skins(Collections.singletonList(skin), 1, Language.Portuguese);
        Skins instance2 = new Skins(Collections.singletonList(skin), 1, Language.Portuguese);

        storage.store(instance1);
        storage.store(instance2);

        assert storage.getSkinStorage()
                .getAsList()
                .stream()
                .filter((stored -> stored.getLanguage() == Language.Portuguese))
                .filter((stored) -> stored.getChampionId() == 1)
                .count() == 1;
    }

    @Test
    @DisplayName("Add multi-language skins to storage")
    @Order(3)
    void Add_Multi_Language_Skins_To_Storage() {
        PaladinsStorageImpl storage = (PaladinsStorageImpl) PaladinsStorageImpl.getInstance();
        ChampionSkin skin = new ChampionSkinTestImpl(1, 1, Language.Portuguese);
        Skins portugueseChampions = new Skins(Collections.singletonList(skin), 1, Language.Portuguese);
        Skins englishChampions = new Skins(Collections.singletonList(skin), 1, Language.English);

        storage.store(portugueseChampions);
        storage.store(englishChampions);

        assert storage.getSkinStorage().size() == 2;
    }

    @Test
    @DisplayName("Get skins from storage by championId and language")
    @Order(4)
    void Get_Champions_From_Storage_By_Language() {
        PaladinsStorageImpl storage = (PaladinsStorageImpl) PaladinsStorageImpl.getInstance();
        ChampionSkin skin = new ChampionSkinTestImpl(1, 1, Language.Portuguese);
        Skins portugueseChampions = new Skins(Collections.singletonList(skin), 1, Language.Portuguese);

        storage.store(portugueseChampions);
        assert storage.getSkinFromStorage(1, Language.Portuguese) != null;
    }
}
