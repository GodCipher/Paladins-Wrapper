package me.skiincraft.api.paladins.entity.champions.objects;

import me.skiincraft.api.paladins.internal.CustomList;
import me.skiincraft.api.paladins.objects.champion.Card;
import me.skiincraft.api.paladins.objects.miscellany.Language;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h1>Cards</h1>
 * <p>
 * <p>Class that will hold all the cards of any champion</p>
 * <p>No items can be removed from this class</p>
 * </p>
 */
public class Cards implements CustomList<Card> {

    private final Card[] cards;
    private final long championId;
    private final Language language;

    public Cards(List<Card> card, long championId, Language language) {
        this.cards = new Card[card.size()];
        this.championId = championId;
        this.language = language;
        AtomicInteger integer = new AtomicInteger();
        for (Card item : card) {
            cards[integer.getAndIncrement()] = item;
        }
    }

    @Nonnull
    public Iterator<Card> iterator() {
        return Arrays.stream(cards).iterator();
    }

    public List<Card> getAsList() {
        return Arrays.stream(cards).collect(Collectors.toList());
    }

    public Stream<Card> getAsStream() {
        return Arrays.stream(cards);
    }

    public Card getById(long id) {
        return getAsStream().filter(o -> o.getCardId2() == id).findFirst().orElse(null);
    }

    public Language getCardsLanguage() {
        return language;
    }

    public long getChampionCardId() {
        return championId;
    }

    @Override
    public String toString() {
        return "Cards{" +
                "cards=" + cards.length +
                ", championId=" + championId +
                ", language=" + language +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cards cards = (Cards) o;
        return championId == cards.championId && language == cards.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(championId, language);
    }
}
