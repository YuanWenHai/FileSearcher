package com.will.filesearcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.will.filesearcher.filter.FileFilter;
import com.will.filesearcher.searchengine.FileItem;
import com.will.filesearcher.searchengine.SearchEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 2017/10/31.
 */

public class FileSearcherActivity extends AppCompatActivity{
    private SearchEngine searchEngine;
    private FileSearcherAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private List<FileItem> selectedItems = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_searcher_main);
        initializeView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeSearchEngine();
    }

    private void initializeView(){
        toolbar = findViewById(R.id.file_searcher_main_toolbar);
        toolbar.setNavigationIcon(R.drawable.back_holo_dark_no_trim_no_padding);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fab = findViewById(R.id.file_searcher_main_fab);
        RecyclerView recyclerView  = findViewById(R.id.file_searcher_main_recycler_view);
        adapter = new FileSearcherAdapter(this);
        adapter.setOnItemSelectCallback(new FileSearcherAdapter.OnItemSelectCallback() {
            @Override
            public void onSelect(FileItem item, boolean which) {
                if(which){
                    selectedItems.add(item);
                }else{
                    selectedItems.remove(item);
                }
                toolbar.setTitle(selectedItems.size()+"/"+adapter.getItemCount());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
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
            public void onFind(FileItem item) {
                adapter.addItem(item);
            }

            @Override
            public void onSearchDirectory(File file) {
                toolbar.setTitle(file.getName());
            }

            @Override
            public void onFinish() {
                toolbar.setTitle("0/"+adapter.getItemCount());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_searcher_activity_menu,menu);
        return true;
    }
}
