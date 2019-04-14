package my.base.parser;

public class ParseException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public ParseException(String msg) {
        super(msg);
    }

    public ParseException(String msg, Throwable e) {
        super(msg, e);
    }

    public ParseException(Throwable e) {
        super(e);
    }
}
