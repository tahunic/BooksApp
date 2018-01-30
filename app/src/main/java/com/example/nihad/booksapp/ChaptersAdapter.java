package com.example.nihad.booksapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by nihad on 30/01/2018.
 */

public class ChaptersAdapter extends RecyclerView.Adapter<ChaptersAdapter.MyViewHolder> {

    LinkedHashMap<String, Integer> chapterPages;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView chapterName, pageNumber, page;

        public MyViewHolder(View view) {
            super(view);
            chapterName = (TextView) view.findViewById(R.id.chapterName);
            pageNumber = (TextView) view.findViewById(R.id.pageNumber);
            page = (TextView) view.findViewById(R.id.page);
        }
    }

    public ChaptersAdapter(LinkedHashMap<String, Integer> chapterPages) {
        this.chapterPages = chapterPages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Integer page = (new ArrayList<Integer>(chapterPages.values())).get(position);
        String chapter = (new ArrayList<String>(chapterPages.keySet())).get(position);

        holder.chapterName.setText(chapter);
        holder.pageNumber.setText("Stranica - " + (page + 1));
//        holder.page.setText(String.valueOf(page));
    }

    public String getItem(int position) {
        return (new ArrayList<String>(chapterPages.keySet())).get(position);
    }

    @Override
    public int getItemCount() {
        return chapterPages.size();
    }

}
