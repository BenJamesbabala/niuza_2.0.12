package my.base.io;

import java.util.ArrayList;
import my.base.util.StringUtils;

public class FileSearchJob {
    private boolean isCanceled = false;
    private boolean isFinished = false;
    private MyFileFilter searchFileFilter;
    private ArrayList<String> searchPathList = new ArrayList();
    private ArrayList<MyFile> searchedFileList = new ArrayList();
    private int searchedFileNum = 0;
    private long searchedSize = 0;
    private String searchingFile = "";
    private int totalFileNum = 0;

    public ArrayList<MyFile> getSearchedFileList() {
        return this.searchedFileList;
    }

    public void setSearchedFileList(ArrayList<MyFile> searchedFileList) {
        this.searchedFileList = searchedFileList;
    }

    public MyFileFilter getSearchFileFilter() {
        return this.searchFileFilter;
    }

    public void setSearchFileFilter(MyFileFilter searchFileFilter) {
        this.searchFileFilter = searchFileFilter;
    }

    public long getSearchedSize() {
        return this.searchedSize;
    }

    public void setSearchedSize(long searchedSize) {
        this.searchedSize = searchedSize;
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

    public int getTotalFileNum() {
        return this.totalFileNum;
    }

    public void setTotalFileNum(int totalFileNum) {
        this.totalFileNum = totalFileNum;
    }

    public int getSearchedFileNum() {
        return this.searchedFileNum;
    }

    public void setSearchedFileNum(int searchedFileNum) {
        this.searchedFileNum = searchedFileNum;
    }

    public ArrayList<String> getSearchPathList() {
        return this.searchPathList;
    }

    public void setSearchPathList(ArrayList<String> searchPathList) {
        this.searchPathList = searchPathList;
    }

    public String getSearchingFile() {
        return this.searchingFile;
    }

    public void setSearchingFile(String searchingFile) {
        this.searchingFile = searchingFile;
    }

    public void addSearchPath(String path) {
        if (StringUtils.isNotBlank(path)) {
            this.searchPathList.add(path);
        }
    }

    public void addSearchedFile(MyFile myFile) {
        if (myFile != null) {
            this.searchedFileList.add(myFile);
        }
    }
}
