package com.example.nihad.booksapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nihad on 27/01/2018.
 */

public class CardAdapter extends PagerAdapter {

    List<Integer> listImages;
    Context context;
    LayoutInflater layoutInflater;

    public CardAdapter(List<Integer> listImages, Context context) {
        this.listImages = listImages;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.card_item, container, false);
        final ImageView imageView = view.findViewById(R.id.cardBook);
        imageView.setImageResource(listImages.get(position));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookActivity = new Intent(context, BookActivity.class);
                bookActivity.putExtra(BookActivity.BOOK_POSITION, position);
                context.startActivity(bookActivity);
            }
        });

        container.addView(view);
        return view;
    }
}
