package com.will.filesearcher.file_searcher;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private List<FileDetail> resultList = new ArrayList<>();
    private List<Boolean> checkStatusMap = new ArrayList<>();
    private FileSearcher fileSearcher;
    private RecyclerView mRecyclerView;
    private boolean isFinished;
    private int colorAccent;
    FSAdapter(final Context context, final TextView textView){
        this.context = context;
        colorAccent = fetchAccentColor();
        fileSearcher = new FileSearcher(new FileSearcher.Callback() {
            @Override
            public void onSearch(String pathName) {
                textView.setText((context.getText(R.string.searching)+pathName));
            }

            @Override
            public void onFind(FileDetail file) {
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
        FileDetail file = resultList.get(position);
        holder.title.setText(context.getString(R.string.file_name)+file.getName());
        holder.location.setText(context.getString(R.string.file_path)+file.getPath());
        holder.size.setText(context.getString(R.string.file_size)+file.getSize());
        holder.time.setText(context.getString(R.string.file_last_modified_time)+file.getLastModifiedTime());
        CardView cardView = (CardView) holder.itemView;
        if(checkStatusMap.get(position)){
            cardView.setCardBackgroundColor(colorAccent);
        }else{
            cardView.setCardBackgroundColor(context.getResources().getColor(R.color.cardview_light_background));
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
                    notifyDataSetChanged();
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
                list.add(resultList.get(i).getFile());
            }
        }
        return list;
    }
    private int fetchAccentColor() {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}
