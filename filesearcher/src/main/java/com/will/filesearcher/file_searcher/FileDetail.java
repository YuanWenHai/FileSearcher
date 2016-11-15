package com.will.filesearcher.file_searcher;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by will on 2016/11/15.
 */

public class FileDetail {
    private String name,path,size,lastModifiedTime;

    private File file;

    public FileDetail(File file){
        this.file = file;
        name = file.getName();
        path = file.getPath();
        size = getSizeWithSuitableUnit();
        lastModifiedTime = getLastModifiedTimeString();
    }


    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getSize() {
        return size;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private String getSizeWithSuitableUnit(){
        float fileSize = file.length();
        int kb = 1;
        int mb = 2;
        int gb = 3;
        int i = 0;
        String unit = "";
        while(fileSize >= 1024){
            fileSize = fileSize / 1024;
            i++;
        }
        DecimalFormat format = new DecimalFormat("#.##");
        StringBuilder builder = new StringBuilder();
        builder.append(format.format(fileSize));
        if(i == 0){
            unit = "byte";
        }else if(i == kb){
            unit = "kb";
        }else if(i == mb){
            unit = "mb";
        }else if(i == gb){
            unit = "gb";
        }
        builder.append(unit);
        return builder.toString();
    }
    private String getLastModifiedTimeString(){
        Date date = new Date(file.lastModified());
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss");
        return format.format(date);
    }
}
