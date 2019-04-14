package my.base.cache;

public class CacheException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public CacheException(String msg) {
        super(msg);
    }

    public CacheException(String msg, Throwable e) {
        super(msg, e);
    }

    public CacheException(Throwable e) {
        super(e);
    }
}
