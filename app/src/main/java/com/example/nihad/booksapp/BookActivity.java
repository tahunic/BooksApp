package com.example.nihad.booksapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

    public final static String PAGE = "PAGE";
    public final static String BOOK_POSITION = "BOOK_POSITION";

    @BindView(R.id.pages)
    ViewPager pagesView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ActionMenuItemView menuItem;

    private LinkedHashMap<String, Integer> chapterPages;
    private DbHelper dbHelper;
    private Integer page;
    private Integer currentPage;
    private Integer bookPosition;
    private BookLoader bookLoader;

    public static String currentBook;
    public static Integer pageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);

        dbHelper = new DbHelper(this);
        chapterPages = new LinkedHashMap<>();
        page = getIntent().getIntExtra(PAGE, 0);
        bookLoader = new BookLoader(this);
        bookLoader.insertData();
        setCurrentBook();

        setPageViewer();
        setToolbar();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(currentBook);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_bookmark:
                        toggleBookmark(); break;
                    case R.id.action_changeBook:
                        loadBooksList(); break;
                    case R.id.action_tabs:
                        openTabs(); break;
                    case R.id.action_goToPage:
                        goToPage(); break;

                }

                return true;
            }
        });
    }

    private void openTabs() {
        Intent tabbedActivity = new Intent(this, TabbedActivity.class);
        tabbedActivity.putExtra(TabbedActivity.CHAPTER_PAGES, new Gson().toJson(chapterPages));
        startActivity(tabbedActivity);
    }

    private void loadBooksList() {
        Intent booksCardsActivity = new Intent(this, BooksCardsActivity.class);
        startActivity(booksCardsActivity);
    }

    private void goToPage() {
        RelativeLayout linearLayout = new RelativeLayout(BookActivity.this);
        final NumberPicker numberPicker = new NumberPicker(BookActivity.this);
        numberPicker.setMaxValue(BookActivity.pageCount + 1);
        numberPicker.setMinValue(1);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        linearLayout.setLayoutParams(params);
        linearLayout.addView(numberPicker,numPicerParams);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BookActivity.this);
        alertDialogBuilder.setTitle("Odaberi stranicu");
        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent bookActivity = new Intent(BookActivity.this, BookActivity.class);
                                bookActivity.putExtra(BookActivity.PAGE, numberPicker.getValue() - 1);
                                startActivity(bookActivity);
                            }
                        })
                .setNegativeButton("Poništi",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("RestrictedApi")
    private void toggleBookmark() {
        menuItem = findViewById(R.id.action_bookmark);

        String currentChapter = "";
        for (int i = 0; i < chapterPages.size() - 1; i++) {
            if(currentPage >= (new ArrayList<Integer>(chapterPages.values())).get(i) && currentPage < (new ArrayList<Integer>(chapterPages.values())).get(i + 1)){
                currentChapter = (new ArrayList<String>(chapterPages.keySet())).get(i);
                break;
            }
        }

        if(!dbHelper.bookmarkExists(currentPage.toString())){
            dbHelper.insertBookmarkData(currentPage, currentChapter, currentBook);

            Toast.makeText(this, "Stranica označena", Toast.LENGTH_SHORT).show();
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_white));
        } else {
            dbHelper.deleteBookmarkData(currentPage.toString());

            Toast.makeText(this, "Oznaka uklonjena", Toast.LENGTH_SHORT).show();
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border_white));
        }

    }

    private void setCurrentBook() {
        bookPosition = getIntent().getIntExtra(BOOK_POSITION, -1);

        if(bookPosition == -1)
            return;

        switch (bookPosition){
            case 0:
                currentBook = "Knjiga o nepravednim ljudima"; break;
            case 1:
                currentBook = "Najstrpljiviji zatvorenik"; break;
            case 2:
                currentBook = "Vodič kroz život"; break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_menu, menu);
        return true;
    }

    private void setPageViewer() {
        pagesView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                PageSplitter pageSplitter = new PageSplitter(pagesView.getWidth(), pagesView.getHeight(), 1, 0);

                TextPaint textPaint = new TextPaint();
                textPaint.setTextSize(getResources().getDimension(R.dimen.text_size));


                Cursor result = dbHelper.getChapterData();
                if(result.getCount() != 0){
                    while(result.moveToNext()){
                        if(result.getString(3).contentEquals(currentBook)){
                            textPaint.setFakeBoldText(true);

                            String title = result.getString(1);
                            pageSplitter.append(title + "\n\n", textPaint);
                            chapterPages.put(title, pageSplitter.getPages().size() - 1);

                            textPaint.setFakeBoldText(false);
                            pageSplitter.append(result.getString(2), textPaint);

                            pageSplitter.pageBreak();
                        }

                    }

                }
                pageCount = pageSplitter.getCount();
                pagesView.setAdapter(new TextPagerAdapter(getSupportFragmentManager(), pageSplitter.getPages()));
                pagesView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                pagesView.setCurrentItem(page);

            }
        });

        pagesView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getSupportActionBar().setSubtitle("Stranica " + (position + 1));
                currentPage = position;
                menuItem = findViewById(R.id.action_bookmark);

                if(menuItem != null) {
                    if(!dbHelper.bookmarkExists(currentPage.toString())){
                        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border_white));
                    } else {
                        menuItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_white));
                    }
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


}
