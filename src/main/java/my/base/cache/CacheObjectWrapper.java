package my.base.cache;

import java.util.Date;

public class CacheObjectWrapper {
    private long expirTime = 0;
    private Object object;
    private Date refreshTime;

    public CacheObjectWrapper(Object object, Date refreshTime, long expirTime) {
        this.object = object;
        if (refreshTime == null) {
            this.refreshTime = new Date();
        } else {
            this.refreshTime = refreshTime;
        }
        this.expirTime = expirTime;
    }

    public Object getObject() {
        return this.object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Date getRefreshTime() {
        return this.refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }

    public long getExpirTime() {
        return this.expirTime;
    }

    public void setExpirTime(long expirTime) {
        this.expirTime = expirTime;
    }
}
