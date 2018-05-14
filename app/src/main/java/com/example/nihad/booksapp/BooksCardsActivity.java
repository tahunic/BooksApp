package com.example.nihad.booksapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;

public class BooksCardsActivity extends AppCompatActivity {

    List<Integer> listImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_cards);

        initData();

        HorizontalInfiniteCycleViewPager pager = findViewById(R.id.horizontal_cycle);
        CardAdapter adapter = new CardAdapter(listImages, BooksCardsActivity.this);

        pager.setAdapter(adapter);
    }

    private void initData() {
        listImages.add(R.drawable.vodic_kroz_zivot);
        listImages.add(R.drawable.knjiga_o_nepravednim_ljudima);
        listImages.add(R.drawable.najstrpljiviji_zatvorenik_u_historiji);
    }
}
