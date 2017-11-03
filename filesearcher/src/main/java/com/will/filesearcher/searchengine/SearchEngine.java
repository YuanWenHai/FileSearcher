package com.will.filesearcher.searchengine;

import android.os.Handler;
import android.os.Looper;

import com.will.filesearcher.filter.FileFilter;

import java.io.File;

/**
 * Created by Will on 2017/11/1.
 */

public class SearchEngine {
    private final File path;
    private final FileFilter filter;
    private final android.os.Handler handler;

    private Thread worker;
    public SearchEngine(File path, FileFilter filter){
        this.path = path;
        this.filter = filter;
        handler = new Handler(Looper.myLooper());
    }
    public void start(final SearchEngineCallback callback){
        worker = new Thread(new Runnable() {
            @Override
            public void run() {
                findFileRecursively(path,callback);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFinish();
                    }
                });
            }
        });
        worker.start();
    }
    public void stop(){
        if(worker != null){
            worker.interrupt();
        }
    }

    private void findFileRecursively(final File file,final SearchEngineCallback callback){
        //Log.d("file name",file.getName());
        if(file.isDirectory() ){
            File[] files = file.listFiles();
            if(files != null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSearchDirectory(file);
                    }
                });
                for(File f : files){
                    findFileRecursively(f,callback);
                }
            }
        }else{
            if(filter.filter(file)){
                final FileItem item = new FileItem(file);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFind(item);
                    }
                });

            }
        }
    }
    public interface SearchEngineCallback{
        void onFind(FileItem fileItem);
        void onSearchDirectory(File file);
        void onFinish();
    }

}
