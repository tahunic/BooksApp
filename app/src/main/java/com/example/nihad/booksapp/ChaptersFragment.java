package com.example.nihad.booksapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChaptersFragment extends Fragment {

    LinkedHashMap<String, Integer> chapterPages;
    private RecyclerView chaptersRecyclerView;
    private ChaptersAdapter chaptersAdapter;

    public ChaptersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chapters, container, false);

        Type entityType = new TypeToken< LinkedHashMap<String, Integer>>(){}.getType();
        chapterPages = new Gson().fromJson(getArguments().getString(TabbedActivity.CHAPTER_PAGES), entityType);

        chaptersAdapter = new ChaptersAdapter(chapterPages);
        chaptersRecyclerView = view.findViewById(R.id.chaptersRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        chaptersRecyclerView.setLayoutManager(mLayoutManager);
        chaptersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chaptersRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        chaptersRecyclerView.setAdapter(chaptersAdapter);

        chaptersRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity().getApplicationContext(), chaptersRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String  itemValue    = chaptersAdapter.getItem(position);

                        Intent bookActivity = new Intent(getActivity(), BookActivity.class);
                        bookActivity.putExtra(BookActivity.PAGE, chapterPages.get(itemValue));
                        startActivity(bookActivity);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        return view;



    }

}
