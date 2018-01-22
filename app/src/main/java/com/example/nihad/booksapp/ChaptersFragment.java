package com.example.nihad.booksapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.LinkedHashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChaptersFragment extends Fragment {

    LinkedHashMap<String, Integer> chapterPages;
    ListView chaptersListView ;

    public ChaptersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chapters, container, false);

        Type entityType = new TypeToken< LinkedHashMap<String, Integer>>(){}.getType();
        chapterPages = new Gson().fromJson(getArguments().getString(TabbedActivity.CHAPTER_PAGES), entityType);

        chaptersListView = view.findViewById(R.id.chaptersList);

//        String[] values = new String[] { "Android List View",
//                "Adapter implementation",
//                "Simple List View In Android",
//                "Create List View Android",
//                "Android Example",
//                "List View Source Code",
//                "List View Array Adapter",
//                "Android Example List View"
//        };

        String[] values = chapterPages.keySet().toArray(new String[0]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.chapter_list_item, R.id.chapterName, values);

        chaptersListView.setAdapter(adapter);
        chaptersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item value
                String  itemValue    = (String) chaptersListView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getContext(),
                        "Stranica :"+chapterPages.get(itemValue)+"  ListItem : " + itemValue , Toast.LENGTH_LONG)
                        .show();

                Intent bookActivity = new Intent(getActivity(), BookActivity.class);
                bookActivity.putExtra(BookActivity.PAGE, chapterPages.get(itemValue));
                startActivity(bookActivity);
            }
        });

        return view;
    }

}
