package my.base.io;

import java.io.File;
import java.io.FilenameFilter;
import my.base.io.MyFile.FILE_TYPE;

public class MyFileFilter implements FilenameFilter {
    private FILE_TYPE fileType = FILE_TYPE.NORMAL;

    public MyFileFilter(FILE_TYPE fileType) {
        this.fileType = fileType;
    }

    public FILE_TYPE getFileType() {
        return this.fileType;
    }

    public void setFileType(FILE_TYPE fileType) {
        this.fileType = fileType;
    }

    public boolean accept(File dir, String name) {
        return true;
    }
}
