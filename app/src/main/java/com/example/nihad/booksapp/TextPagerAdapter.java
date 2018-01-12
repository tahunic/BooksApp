package com.example.nihad.booksapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

public class TextPagerAdapter extends FragmentPagerAdapter {
    private final List<CharSequence> pageTexts;

    public TextPagerAdapter(FragmentManager fm, List<CharSequence> pageTexts) {
        super(fm);
        this.pageTexts = pageTexts;
    }

    @Override
    public Fragment getItem(int i) {
        Log.d("Stranica", String.valueOf(i));
        return PageFragment.newInstance(pageTexts.get(i));
    }

    @Override
    public int getCount() {
        return pageTexts.size();
    }
}