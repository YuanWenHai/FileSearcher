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
    private boolean stopSearching;
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onFind(file);
                        }
                    });
                }
            }else{
                searchFile(file,keyword);
            }
        }
    }
    public interface Callback {
        void onSearch(String pathName);
        void onFind(File file);
        void onFinish();
    }
    public void stopSearching(boolean which){
        stopSearching = which;
    }
}
