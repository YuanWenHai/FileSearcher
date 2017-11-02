package com.will.filesearcher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.will.filesearcher.util.FileSearcherUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Will on 2017/11/1.
 */

public class FileSearcherAdapter extends RecyclerView.Adapter<FileSearcherAdapter.FileSearcherVH> {
    private List<File> files = new ArrayList<>();
    @Override
    public FileSearcherVH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FileSearcherVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.file_searcher_item,parent,false));
    }

    @Override
    public void onBindViewHolder(FileSearcherVH holder, int position) {
        File file = files.get(position);
        holder.title.setText(file.getName());
        holder.location.setText(file.getPath());
        holder.size.setText(FileSearcherUtil.byteSizeFormatter(file.length()));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }
    public void addItem(File file){
        files.add(file);
        notifyDataSetChanged();
    }

    class FileSearcherVH extends RecyclerView.ViewHolder{
        TextView title,size,location;
        public FileSearcherVH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.file_searcher_item_text_title);
            size = itemView.findViewById(R.id.file_searcher_item_text_size);
            location = itemView.findViewById(R.id.file_searcher_item_text_location);
        }
    }
}
