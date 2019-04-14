package my.base.net;

public class NetException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public NetException(String msg) {
        super(msg);
    }

    public NetException(String msg, Throwable e) {
        super(msg, e);
    }

    public NetException(Throwable e) {
        super(e);
    }
}
