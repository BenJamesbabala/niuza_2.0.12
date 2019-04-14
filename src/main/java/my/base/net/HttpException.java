package my.base.net;

public class HttpException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public HttpException(String msg) {
        super(msg);
    }

    public HttpException(String msg, Throwable e) {
        super(msg, e);
    }

    public HttpException(Throwable e) {
        super(e);
    }
}
