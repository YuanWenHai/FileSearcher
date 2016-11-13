package com.will.filesearcher.file_searcher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.will.filesearcher.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by will on 2016/10/29.
 */

public class FSAdapter extends RecyclerView.Adapter<FSAdapter.FSViewHolder> {
    private Context context;
    private List<File> resultList = new ArrayList<>();
    private List<Boolean> checkStatusMap = new ArrayList<>();
    private FileSearcher fileSearcher;
    private RecyclerView mRecyclerView;
    private boolean isFinished;
    FSAdapter(final Context context, final TextView textView){
        this.context = context;
        fileSearcher = new FileSearcher(new FileSearcher.Callback() {
            @Override
            public void onSearch(String pathName) {
                textView.setText((context.getText(R.string.searching)+pathName));
            }

            @Override
            public void onFind(File file) {
                resultList.add(file);
                checkStatusMap.add(false);
                mRecyclerView.smoothScrollToPosition(resultList.size());
                notifyDataSetChanged();
            }
            @Override
            public void onFinish(){

                if(resultList.size() == 0){
                    textView.setText(context.getText(R.string.no_result));
                }else{
                    textView.setText(context.getText(R.string.search_finish));
                }
                isFinished = true;
                notifyDataSetChanged();
            }
        });
    }
    public void startSearch(File dir,String keyword){
        fileSearcher.startSearch(dir,keyword);

    }
    public void setMax(long max){
        fileSearcher.setMaxSize(max);
    }
    public void setMin(long min){
        fileSearcher.setMinSize(min);
    }
    @Override
    public int getItemCount() {
        return resultList.size();
    }

    @Override
    public FSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.file_searcher_item,parent,false);
        return new FSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FSViewHolder holder, int position) {
        File file = resultList.get(position);
        holder.title.setText(file.getName());
        holder.location.setText(file.getPath());
        holder.size.setText(file.);
        if(isFinished){
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(checkStatusMap.get(position));
        }else{
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
    }

    class FSViewHolder extends RecyclerView.ViewHolder{
        TextView title,location,size,time;
        FSViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.file_searcher_item_title);
            location = (TextView) view.findViewById(R.id.file_searcher_item_location);
            size = (TextView) view.findViewById(R.id.file_searcher_item_size);
            time = (TextView) view.findViewById(R.id.file_searcher_item_create_time);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkStatusMap.set(getAdapterPosition(),!checkStatusMap.get(getAdapterPosition()));
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }
    public void stopSearching(boolean which){
        fileSearcher.stopSearching(which);
    }
    public boolean isSearching(){
        return !isFinished;
    }
    public void changeAllCheckBoxStatus(){

        for(Boolean b :checkStatusMap){
            if(b){
                for(int i=0;i<checkStatusMap.size();i++){
                    checkStatusMap.set(i,false);
                }
                 notifyDataSetChanged();
                return;
            }
        }

        for(int i=0;i<checkStatusMap.size();i++){
            checkStatusMap.set(i,true);
        }
        notifyDataSetChanged();
    }
    public List<File> getSelectedItems(){
        List<File> list = new ArrayList<>();
        for(int i=0;i<checkStatusMap.size();i++){
            if(checkStatusMap.get(i)){
                list.add(resultList.get(i));
            }
        }
        return list;
    }
    private String getSizeWithSuitableUnit(File file){
        float fileSize = file.length();
        int kb = 1;
        int mb = 2;
        int gb = 3;
        int i = 0;
        while (fileSize > 1){
            fileSize = fileSize / 1024;
            i++;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(fileSize);
        if(i<)
    }
}
