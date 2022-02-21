package me.skiincraft.api.paladins.storage.impl;

import me.skiincraft.api.paladins.entity.champions.Champions;
import me.skiincraft.api.paladins.entity.champions.objects.Cards;
import me.skiincraft.api.paladins.entity.champions.objects.Skins;
import me.skiincraft.api.paladins.entity.match.Match;
import me.skiincraft.api.paladins.internal.logging.PaladinsLogger;
import me.skiincraft.api.paladins.objects.miscellany.Language;
import me.skiincraft.api.paladins.storage.PaladinsStorage;
import me.skiincraft.api.paladins.storage.Storage;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;

/**
 * <p>Is the PaladinsStorage implementation class</p>
 */
public class PaladinsStorageImpl implements PaladinsStorage {

    private final Storage<Champions> championMemory;
    private final Storage<Match> matchMemory;
    private final Storage<Cards> cardsMemory;
    private final Storage<Skins> skinMemory;

    private static final PaladinsStorage instance = getInstance();
    private static final Logger logger = PaladinsLogger.getLogger(PaladinsStorage.class);

    public PaladinsStorageImpl(Storage<Champions> championMemory, Storage<Match> matchMemory, Storage<Cards> cardsMemory, Storage<Skins> skinMemory) {
        this.championMemory = championMemory;
        this.matchMemory = matchMemory;
        this.cardsMemory = cardsMemory;
        this.skinMemory = skinMemory;
    }

    public Storage<Champions> getChampionsStorage() {
        return championMemory;
    }

    public Storage<Match> getMatchStorage() {
        return matchMemory;
    }

    public Storage<Cards> getCardsStorage() {
        return cardsMemory;
    }

    @Override
    public Storage<Skins> getSkinStorage() {
        return skinMemory;
    }

    @Nullable
    @Override
    public Champions getChampionsFromStorage(Language language) {
        return championMemory.getById(language.getLanguagecode());
    }

    @Nullable
    @Override
    public Cards getCardsFromStorage(long championId, Language language) {
        return cardsMemory.getAsList().stream()
                .filter(cards -> cards.getCardsLanguage() == language)
                .filter(cards -> cards.getChampionCardId() == championId).findFirst().orElse(null);
    }

    @Nullable
    @Override
    public Match getMatchFromStorage(long matchId) {
        return matchMemory.getById(matchId);
    }

    @Nullable
    @Override
    public Skins getSkinFromStorage(long championId, Language language) {
        return skinMemory.getAsList().stream()
                .filter(skin -> skin.get(0).getLanguage() == language)
                .filter(skin -> skin.get(0).getChampionId() == championId).findFirst().orElse(null);
    }

    /**
     * <p>This method is used to add Champions to the Storage</p>
     */
    public synchronized void store(Champions champion) {
        StorageImpl<Champions> storage = (StorageImpl<Champions>) championMemory;
        List<Champions> data = storage.getAsList();
        data.removeIf(champion::equals);
        data.add(champion);
        logger.debug("Champions: Added a new collection of champions in storage");
        logger.debug("Champions: Size={}, Language='{}'", champion.size(), champion.getLanguage());

        storage.lastupdate = System.currentTimeMillis();
        storage.item = data;
    }

    /**
     * <p>This method is used to add Match to the Storage</p>
     */
    public synchronized void store(Match match) {
        StorageImpl<Match> storage = (StorageImpl<Match>) matchMemory;
        List<Match> data = storage.getAsList();
        data.removeIf(match::equals);
        data.add(match);
        logger.debug("Match: Added a new match in storage");
        logger.debug("Match: MatchId={}, Queue='{}'", match.getMatchId(), match.getQueue());

        storage.lastupdate = System.currentTimeMillis();
        storage.item = data;
    }

    /**
     * <p>This method is used to add Cards to the Storage</p>
     */
    public synchronized void store(Cards cards) {
        if (cards.size() == 0) {
            return;
        }
        StorageImpl<Cards> storage = (StorageImpl<Cards>) cardsMemory;
        List<Cards> data = storage.getAsList();
        data.removeIf(cards::equals);
        data.add(cards);
        logger.debug("Cards: Added a new collections of cards in storage");
        logger.debug("Cards: ChampionId={}, Language='{}'", cards.getChampionCardId(), cards.get(0).getLanguage());

        storage.lastupdate = System.currentTimeMillis();
        storage.item = data;
    }

    public synchronized void store(Skins skin) {
        StorageImpl<Skins> storage = (StorageImpl<Skins>) skinMemory;
        List<Skins> data = storage.getAsList();
        data.removeIf(skin::equals);
        data.add(skin);
        logger.debug("Skins: Added a new collections of skins in storage");
        logger.debug("Skins: ChampionId={}, Language='{}'", skin.getChampionId(), skin.get(0).getLanguage());

        storage.lastupdate = System.currentTimeMillis();
        storage.item = data;
    }

    @Override
    public String toString() {
        return "PaladinsStorage{" +
                "championMemory=" + championMemory.size() +
                ", matchMemory=" + matchMemory.size() +
                ", cardsMemory=" + cardsMemory.size() +
                ", skinMemory=" + skinMemory.size() +
                '}';
    }

    public static PaladinsStorage getInstance() {
        return (instance == null) ? new PaladinsStorageImpl(new ChampionsStorage(), new MatchStorage(), new CardsStorage(), new SkinsStorage()) : instance;
    }
}
