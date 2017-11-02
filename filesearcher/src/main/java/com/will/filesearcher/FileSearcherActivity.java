package com.will.filesearcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.will.filesearcher.filter.FileFilter;
import com.will.filesearcher.searchengine.SearchEngine;

import java.io.File;

/**
 * Created by Will on 2017/10/31.
 */

public class FileSearcherActivity extends AppCompatActivity{
    private SearchEngine searchEngine;
    private FileSearcherAdapter adapter;
    private Toolbar toolbar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_searcher_main);
        initializeView();
        initializeSearchEngine();
    }
    private void initializeView(){
        toolbar = findViewById(R.id.file_searcher_main_toolbar);
        RecyclerView recyclerView  = findViewById(R.id.file_searcher_main_recycler_view);
        adapter = new FileSearcherAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void initializeSearchEngine(){
        FileFilter filter = (FileFilter) getIntent().getSerializableExtra(FileSearcher.FILE_FILTER);
        File path = (File) getIntent().getSerializableExtra(FileSearcher.SEARCH_PATH);
        if(filter == null || path == null){
            throw new NullPointerException("the filter and path cannot be null!");
        }
        searchEngine = new SearchEngine(path,filter);
        searchEngine.start(new SearchEngine.SearchEngineCallback() {
            @Override
            public void onFind(File file) {
                adapter.addItem(file);
            }

            @Override
            public void onSearchDirectory(File file) {
                toolbar.setTitle(file.getName());
            }

            @Override
            public void onFinish() {
                toolbar.setTitle("Search completed");
            }
        });
    }
}
