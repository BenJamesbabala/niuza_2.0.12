package my.base.cache;

import java.util.HashMap;

public class MemCacheProvider implements CacheProvider {
    private static HashMap<String, Cache> regionCache = new HashMap();

    public Cache buildCache(String cacheClassName, String regionName, boolean autoCreate) throws CacheException {
        if (regionCache.containsKey(regionName)) {
            return (MemCache) regionCache.get(regionName);
        }
        if (!autoCreate) {
            return null;
        }
        try {
            MemCache memCache = (MemCache) Class.forName(cacheClassName).newInstance();
            regionCache.put(regionName, memCache);
            return memCache;
        } catch (ClassNotFoundException e) {
            throw new CacheException(new StringBuilder(String.valueOf(cacheClassName)).append(" not found!").toString());
        } catch (InstantiationException e2) {
            throw new CacheException(new StringBuilder(String.valueOf(cacheClassName)).append(" initialize failed!").toString());
        } catch (IllegalAccessException e3) {
            throw new CacheException(new StringBuilder(String.valueOf(cacheClassName)).append(" can not access!").toString());
        }
    }

    public void removeCache(String regionName) throws CacheException {
        ((MemCache) regionCache.get(regionName)).destroy();
        regionCache.remove(regionName);
    }

    public void start() throws CacheException {
    }

    public void stop() {
    }
}
