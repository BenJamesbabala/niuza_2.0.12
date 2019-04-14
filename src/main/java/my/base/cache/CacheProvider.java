package my.base.cache;

public interface CacheProvider {
    Cache buildCache(String str, String str2, boolean z) throws CacheException;

    void removeCache(String str) throws CacheException;

    void start() throws CacheException;

    void stop();
}
