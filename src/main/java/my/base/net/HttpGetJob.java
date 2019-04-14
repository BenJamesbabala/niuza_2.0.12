package my.base.net;

public class HttpGetJob {
    private boolean isCanceled = false;
    private boolean isFinished = false;
    private String result = "";
    private String url = "";

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isCanceled() {
        return this.isCanceled;
    }

    public void setCanceled(boolean isCanceled) {
        this.isCanceled = isCanceled;
    }
}
