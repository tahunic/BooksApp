package com.example.nihad.booksapp;


import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarksFragment extends Fragment {

    private DbHelper dbHelper;
    ListView bookmarkListView ;
//    ArrayList<String> list;


    private RecyclerView bookmarksRecyclerView;
    private BookmarksAdapter bookmarksAdapter;
    private LinkedHashMap<Integer, String> bookmarksList;

    public BookmarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        dbHelper = new DbHelper(getActivity());
        bookmarksList = new LinkedHashMap<>();

        Cursor result = dbHelper.getBookmarkData();
        if(result.getCount() != 0){
            while(result.moveToNext()){
                if(result.getString(3).contentEquals(BookActivity.currentBook)){
                    bookmarksList.put(result.getInt(1), result.getString(2));
                }
            }
        }


        bookmarksAdapter = new BookmarksAdapter(bookmarksList);
        bookmarksRecyclerView = view.findViewById(R.id.bookmarksRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        bookmarksRecyclerView.setLayoutManager(mLayoutManager);
        bookmarksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bookmarksRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        bookmarksRecyclerView.setAdapter(bookmarksAdapter);

        bookmarksRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity().getApplicationContext(), bookmarksRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Integer itemValue    = bookmarksAdapter.getItem(position);

                        Intent bookActivity = new Intent(getActivity(), BookActivity.class);
                        bookActivity.putExtra(BookActivity.PAGE, itemValue);
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
