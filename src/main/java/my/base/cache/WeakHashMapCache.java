package my.base.cache;

import java.util.WeakHashMap;

public class WeakHashMapCache extends MemCache {
    public WeakHashMapCache() {
        this.cache = new WeakHashMap();
    }
}
