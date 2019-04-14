package my.base.cache;

import java.util.List;

public interface Cache {
    void clear() throws CacheException;

    void destroy() throws CacheException;

    Object get(Object obj) throws CacheException;

    boolean has(Object obj) throws CacheException;

    List keys() throws CacheException;

    long memSize() throws CacheException;

    void put(Object obj, Object obj2) throws CacheException;

    void put(Object obj, Object obj2, String str) throws CacheException;

    void remove(Object obj) throws CacheException;

    int size() throws CacheException;
}
