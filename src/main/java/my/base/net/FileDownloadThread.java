package my.base.net;

public class FileDownloadThread {
    private int curPos = 0;
    private int downloadedSize = 0;
    private int endPos = 0;
    private FileDownloadJob fileDownloadJob = null;
    private int id = 0;
    private boolean isCanceled = false;
    private boolean isFinished = false;
    private int jobId = 0;
    private String name = "";
    private int startPos = 0;

    public FileDownloadThread(FileDownloadJob fileDownloadJob) {
        this.fileDownloadJob = fileDownloadJob;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
        this.name = "Thread" + id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStartPos() {
        return this.startPos;
    }

    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }

    public int getEndPos() {
        return this.endPos;
    }

    public void setEndPos(int endPos) {
        this.endPos = endPos;
    }

    public int getCurPos() {
        return this.curPos;
    }

    public void setCurPos(int curPos) {
        this.curPos = curPos;
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

    public int getDownloadedSize() {
        return this.downloadedSize;
    }

    public void setDownloadedSize(int downloadedSize) {
        this.downloadedSize = downloadedSize;
    }

    public FileDownloadJob getFileDownloadJob() {
        return this.fileDownloadJob;
    }

    public void setFileDownloadJob(FileDownloadJob fileDownloadJob) {
        this.fileDownloadJob = fileDownloadJob;
    }

    public void start() {
        new AsyncRemoteFileDownloader(this.fileDownloadJob, this.id).execute(new Integer[]{Integer.valueOf(0)});
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getJobId() {
        return this.jobId;
    }
}
