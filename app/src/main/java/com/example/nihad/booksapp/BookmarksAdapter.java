package com.example.nihad.booksapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by nihad on 30/01/2018.
 */

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.MyViewHolder> {

    LinkedHashMap<Integer, String> bookmarksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView bookmarkName, chapterName, page;

        public MyViewHolder(View view) {
            super(view);
            bookmarkName = view.findViewById(R.id. bookmarkName);
            chapterName = view.findViewById(R.id.chapterName);
            page = view.findViewById(R.id.page);
        }
    }

    public BookmarksAdapter(LinkedHashMap<Integer, String> bookmarksList) {
        this.bookmarksList = bookmarksList;
    }

    @Override
    public BookmarksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bookmark_list_item, parent, false);

        return new BookmarksAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookmarksAdapter.MyViewHolder holder, int position) {
        Integer page = (new ArrayList<Integer>(bookmarksList.keySet())).get(position);
        String chapter = (new ArrayList<String>(bookmarksList.values())).get(position);

        holder.chapterName.setText(chapter);
        holder.bookmarkName.setText("Stranica - " + (page + 1));
//        holder.page.setText(String.valueOf(page));
    }

    public Integer getItem(int position) {
        return (new ArrayList<Integer>(bookmarksList.keySet())).get(position);
    }

    @Override
    public int getItemCount() {
        return bookmarksList.size();
    }
}
