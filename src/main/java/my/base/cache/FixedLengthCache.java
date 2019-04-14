package my.base.cache;

import java.util.LinkedList;
import java.util.List;

public class FixedLengthCache extends MemCache {
    private static int CACHE_LIMIT = 20;
    private LinkedList<Object> history = new LinkedList();

    public void put(Object key, Object value) throws CacheException {
        this.history.add(key);
        put(key, value, null);
    }

    public void put(Object key, Object value, String expirTime) throws CacheException {
        this.history.add(key);
        if (this.history.size() > CACHE_LIMIT) {
            remove(this.history.poll());
        }
        put(key, value);
    }

    public List keys() throws CacheException {
        return this.history;
    }

    public void remove(Object key) throws CacheException {
        this.history.remove(key);
        remove(key);
    }

    public void clear() throws CacheException {
        this.history.clear();
        clear();
    }

    public void destroy() throws CacheException {
        this.history = null;
        destroy();
    }
}
