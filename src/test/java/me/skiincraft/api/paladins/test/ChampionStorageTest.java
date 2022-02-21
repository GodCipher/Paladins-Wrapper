package me.skiincraft.api.paladins.test;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.champions.Champions;
import me.skiincraft.api.paladins.impl.champion.ChampionTestImpl;
import me.skiincraft.api.paladins.storage.impl.PaladinsStorageImpl;
import me.skiincraft.api.paladins.objects.miscellany.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class ChampionStorageTest {

    @Test
    @DisplayName("Add Champions to Storage")
    @Order(1)
    void Add_Champions_To_Storage() {
        PaladinsStorageImpl storage = (PaladinsStorageImpl) PaladinsStorageImpl.getInstance();
        Champion champion = new ChampionTestImpl(1, "Ash");
        Champions champions = new Champions(Collections.singletonList(champion), Language.Portuguese);

        storage.store(champions);
        storage.getChampionsStorage().size();
        assert storage.championsIsStored(Language.Portuguese);
    }

    @Test
    @DisplayName("Add existing champions to storage")
    @Order(2)
    void Add_Existing_Champions_To_Storage() {
        PaladinsStorageImpl storage = (PaladinsStorageImpl) PaladinsStorageImpl.getInstance();
        Champion champion = new ChampionTestImpl(1, "Ash");
        Champions instance1 = new Champions(Collections.singletonList(champion), Language.Portuguese);
        Champions instance2 = new Champions(Collections.singletonList(champion), Language.Portuguese);

        storage.store(instance1);
        storage.store(instance2);

        assert storage.getChampionsStorage()
                .getAsList()
                .stream()
                .filter((stored -> stored.getLanguage() == Language.Portuguese))
                .count() == 1;
    }

    @Test
    @DisplayName("Add multi-language champions to storage")
    @Order(3)
    void Add_Multi_Language_Champions_To_Storage() {
        PaladinsStorageImpl storage = (PaladinsStorageImpl) PaladinsStorageImpl.getInstance();
        Champion champion = new ChampionTestImpl(1, "Ash");
        Champions portugueseChampions = new Champions(Collections.singletonList(champion), Language.Portuguese);
        Champions englishChampions = new Champions(Collections.singletonList(champion), Language.English);

        storage.store(portugueseChampions);
        storage.store(englishChampions);

        assert storage.getChampionsStorage().size() == 2;
    }

    @Test
    @DisplayName("Get champions from storage by language")
    @Order(4)
    void Get_Champions_From_Storage_By_Language() {
        PaladinsStorageImpl storage = (PaladinsStorageImpl) PaladinsStorageImpl.getInstance();
        Champion champion = new ChampionTestImpl(1, "Ash");
        Champions portugueseChampions = new Champions(Collections.singletonList(champion), Language.Portuguese);

        storage.store(portugueseChampions);
        assert storage.getChampionsFromStorage(Language.Portuguese) != null;
    }
}