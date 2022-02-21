package me.skiincraft.api.paladins.storage.impl;

import me.skiincraft.api.paladins.storage.Storage;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * <p>Is the Storage implementation class</p>
 */
public abstract class StorageImpl<T> implements Storage<T> {

    public List<T> item;
    protected long lastupdate;

    public StorageImpl(List<T> item) {
        this.item = item;
    }

    @Nonnull
    public Iterator<T> iterator() {
        return item.iterator();
    }

    public List<T> getAsList() {
        return new ArrayList<>(item);
    }

    public int size() {
        return item.size();
    }

    public long lastUpdate() {
        return lastupdate;
    }

    public abstract T getById(long id);

    @Override
    public String toString() {
        return "Storage{" +
                "item=" + item.size() +
                ", lastupdate=" + lastupdate +
                '}';
    }
}
