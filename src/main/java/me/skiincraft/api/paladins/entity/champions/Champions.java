package me.skiincraft.api.paladins.entity.champions;

import me.skiincraft.api.paladins.internal.CustomList;
import me.skiincraft.api.paladins.objects.miscellany.Language;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Champions implements CustomList<Champion> {

    private final Champion[] champions;
    private final Language language;

    public Champions(List<Champion> champs, Language language) {
        this.champions = new Champion[champs.size()];
        this.language = language;
        AtomicInteger integer = new AtomicInteger();
        for (Champion item : champs) {
            champions[integer.getAndIncrement()] = item;
        }
    }

    @Nonnull
    public Iterator<Champion> iterator() {
        return Arrays.stream(champions).iterator();
    }

    public List<Champion> getAsList() {
        return Arrays.stream(champions).collect(Collectors.toList());
    }

    public Stream<Champion> getAsStream() {
        return Arrays.stream(champions);
    }

    public Champion getById(long id) {
        return getAsStream().filter(o -> o.getId() == id).findFirst().orElse(null);
    }

    public Language getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "Champions{" +
                "champions=" + champions.length +
                ", language=" + language +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Champions champions = (Champions) o;
        return language == champions.language;
    }

    @Override
    public int hashCode() {
        return Objects.hash(language);
    }
}
