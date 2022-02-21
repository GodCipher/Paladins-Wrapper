package me.skiincraft.api.paladins.storage;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * <h1>Storage</h1>
 * <p>It is an immutable "list", containing generic items that can be saved.</p>
 */
public interface Storage<T> extends Iterable<T> {

    /**
     * Returns a copy of the list of T items saved in that class.
     */
    List<T> getAsList();

    /**
     * Quantity of items in the class
     */
    int size();

    /**
     * Returns the time of the last update in milliseconds
     */
    long lastUpdate();

    /**
     * Returns an item by Id, if there is no item with the Id, null will be returned
     */
    @Nullable
    T getById(long id);

}
