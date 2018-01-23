package com.example.nihad.booksapp;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookmarksFragment extends Fragment {

    private DbHelper dbHelper;
    ListView bookmarkListView ;
    ArrayList<String> list;

    public BookmarksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        dbHelper = new DbHelper(getActivity());
        bookmarkListView = view.findViewById(R.id.bookmarksList);

        list = new ArrayList<>();

        Cursor result = dbHelper.getBookmarkData();
        if(result.getCount() != 0){
            while(result.moveToNext()){
                if(result.getString(3).contentEquals("VodicKrozZivot")){
                    list.add(String.valueOf(result.getInt(1) + 1));
                }

            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.bookmark_list_item, R.id.bookmarkName, list);

        bookmarkListView.setAdapter(adapter);
        bookmarkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Integer itemValue = Integer.valueOf(list.get(position)) - 1;

                Intent bookActivity = new Intent(getActivity(), BookActivity.class);
                bookActivity.putExtra(BookActivity.PAGE, itemValue);
                startActivity(bookActivity);
            }
        });


        return view;
    }

}
