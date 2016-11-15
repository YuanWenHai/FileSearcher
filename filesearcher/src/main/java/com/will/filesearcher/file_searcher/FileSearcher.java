package com.will.filesearcher.file_searcher;

import android.os.Handler;
import android.os.Looper;

import java.io.File;

/**
 * Created by will on 2016/10/29.
 */

public class FileSearcher {
    private Callback mCallback;
    private Handler handler;
    private volatile boolean stopSearching;
    private long maxSize = 0;
    private long minSize = 0;
    public FileSearcher(Callback callback){
        mCallback = callback;
        handler = new Handler(Looper.getMainLooper());
    }
    public void startSearch(final File dir, final String keyword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchFile(dir,keyword);
                if(mCallback != null){
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           mCallback.onFinish();
                       }
                   });
                }
            }
        }).start();
    }


    private void searchFile(final File dir, String keyword){
        if(stopSearching){
            if(mCallback != null){
               handler.post(new Runnable() {
                   @Override
                   public void run() {
                       mCallback.onFinish();
                   }
               });
            }
            return;
        }
        if(mCallback != null){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSearch(dir.getName());
                }
            });
        }
        for(final File file : dir.listFiles()){
            if(!file.isDirectory()){
                if(file.getName().toUpperCase().contains(keyword.toUpperCase())){
                    if(maxSize > 0){
                        if(file.length() <= maxSize && file.length() >= minSize ){
                            final FileDetail detail = new FileDetail(file);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mCallback.onFind(detail);
                                }
                            });
                        }
                    }else if(file.length() >= minSize){
                        final FileDetail detail = new FileDetail(file);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onFind(detail);
                            }
                        });
                    }

                }
            }else{
                searchFile(file,keyword);
            }
        }
    }

    public interface Callback {
        void onSearch(String pathName);
        void onFind(FileDetail fileDetail);
        void onFinish();
    }
    public void setMaxSize(long max){
        maxSize = max;
    }
    public void setMinSize(long min){
        minSize = Math.max(min,0);
    }
    public void stopSearching(boolean which){
        stopSearching = which;
    }
}
