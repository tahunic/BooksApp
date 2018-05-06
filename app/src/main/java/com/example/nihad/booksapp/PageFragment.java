package com.example.nihad.booksapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.nihad.booksapp.BookActivity.fontSize;

public class PageFragment extends Fragment {
    private final static String PAGE_TEXT = "PAGE_TEXT";
    private TextView pageNumber;

    public static PageFragment newInstance(CharSequence pageText) {
        PageFragment frag = new PageFragment();
        Bundle args = new Bundle();
        args.putCharSequence(PAGE_TEXT, pageText);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CharSequence text = getArguments().getCharSequence(PAGE_TEXT);
        TextView pageView = (TextView) inflater.inflate(R.layout.page, container, false);
        pageView.setTextSize(fontSize);
        pageView.setText(text);
        return pageView;
    }
}