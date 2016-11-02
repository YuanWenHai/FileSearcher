package com.will.filesearcher.file_searcher;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.will.filesearcher.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by will on 2016/10/29.
 */

public class FileSearcherActivity extends AppCompatActivity {
    public static final int OK = 0;
    public static final int NO_DATA_SELECTED = 1;
    FSAdapter mAdapter;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_searcher_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.file_searcher_main_recycler_view);
        TextView text = (TextView) findViewById(R.id.file_searcher_main_text);
        mAdapter = new FSAdapter(this,text);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String keyword = getIntent().getStringExtra("keyword");
        if(keyword == null){
            throw new IllegalArgumentException("no keyword!");
        }
        mAdapter.startSearch(Environment.getExternalStorageDirectory(),keyword);

        Toolbar toolbar = (Toolbar) findViewById(R.id.file_searcher_main_toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getText(R.string.dialog_message));
        builder.setPositiveButton(getText(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mAdapter.stopSearching(true);
                FileSearcherActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton(getText(R.string.cancel),null);
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if(mAdapter.isSearching()){
            showAlertDialog();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mAdapter.isSearching()){
            return true;
        }
        if(item.getItemId() == R.id.toolbar_menu_done){
            ArrayList<File> selectedData = (ArrayList<File>) mAdapter.getSelectedItems();
            if(selectedData.size() == 0){
                setResult(NO_DATA_SELECTED);
            }else{
                Intent intent = new Intent();
                intent.putExtra("data",selectedData);
                setResult(OK,intent);
            }
            finish();
        }else{
            mAdapter.changeAllCheckBoxStatus();
        }
        return true;
    }
}
