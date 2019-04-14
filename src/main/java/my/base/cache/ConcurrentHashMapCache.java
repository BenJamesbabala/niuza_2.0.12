package my.base.cache;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapCache extends MemCache {
    public ConcurrentHashMapCache() {
        this.cache = new ConcurrentHashMap();
    }
}
