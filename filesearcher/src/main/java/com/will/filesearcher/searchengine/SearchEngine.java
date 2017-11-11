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
    private boolean isSearching;
    private volatile boolean stop;
    private SearchEngineCallback callback;

    public SearchEngine(File path, FileFilter filter){
        this.path = path;
        this.filter = filter;
        handler = new Handler(Looper.myLooper());
    }
    public void start(final SearchEngineCallback callback){
        isSearching = true;
        stop = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                findFileRecursively(path,callback);
                System.out.println(handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFinish();
                    }
                }));
                isSearching = false;
            }
        }).start();
    }
    public void start(){
        if(callback != null){
            start(callback);
        }
    }
    public void stop(){
       stop = isSearching;
    }
    public boolean isSearching(){
        return isSearching;
    }

    public void setCallback(SearchEngineCallback callback){
        this.callback = callback;
    }
    private void findFileRecursively(final File file,final SearchEngineCallback callback){
        if(stop){
            return;
        }
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
