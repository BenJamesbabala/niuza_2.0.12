package my.base.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import my.base.util.TimeUtils;

public class MemCache implements Cache {
    protected Map<Object, CacheObjectWrapper> cache = new HashMap();

    public Object get(Object key) throws CacheException {
        if (key == null) {
            return null;
        }
        CacheObjectWrapper cacheObjectWrapper = getCacheObjectWrapper(key);
        if (cacheObjectWrapper != null) {
            return cacheObjectWrapper.getObject();
        }
        return null;
    }

    private CacheObjectWrapper getCacheObjectWrapper(Object key) {
        CacheObjectWrapper cacheObjectWrapper = (CacheObjectWrapper) this.cache.get(key);
        if (cacheObjectWrapper == null) {
            return null;
        }
        if (cacheObjectWrapper.getExpirTime() <= 0 || TimeUtils.getIntervalInSecond(cacheObjectWrapper.getRefreshTime(), new Date()) <= cacheObjectWrapper.getExpirTime()) {
            return cacheObjectWrapper;
        }
        remove(key);
        return null;
    }

    public void put(Object key, Object value) throws CacheException {
        put(key, value, null);
    }

    public void put(Object key, Object value, String expirTime) throws CacheException {
        if (key != null && value != null) {
            long expirTimeL = 0;
            try {
                expirTimeL = TimeUtils.parseToSecond(expirTime);
            } catch (IllegalArgumentException e) {
            }
            CacheObjectWrapper cacheObjectWrapper = getCacheObjectWrapper(key);
            if (cacheObjectWrapper == null) {
                this.cache.put(key, new CacheObjectWrapper(value, new Date(), expirTimeL));
                return;
            }
            cacheObjectWrapper.setObject(value);
            cacheObjectWrapper.setRefreshTime(new Date());
            cacheObjectWrapper.setExpirTime(expirTimeL);
            this.cache.put(key, cacheObjectWrapper);
        }
    }

    public boolean has(Object key) throws CacheException {
        if (key == null || !this.cache.containsKey(key) || getCacheObjectWrapper(key) == null) {
            return false;
        }
        return true;
    }

    public List keys() throws CacheException {
        List<Object> keyList = new ArrayList();
        for (Object object : this.cache.keySet()) {
            keyList.add(object);
        }
        return keyList;
    }

    public void remove(Object key) throws CacheException {
        this.cache.remove(key);
    }

    public void clear() throws CacheException {
        this.cache.clear();
    }

    public void destroy() throws CacheException {
        this.cache = null;
    }

    public int size() throws CacheException {
        return this.cache.size();
    }

    public long memSize() throws CacheException {
        return 0;
    }
}
