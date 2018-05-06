package com.example.nihad.booksapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookActivity extends AppCompatActivity {

    public final static String PAGE = "PAGE";
    public final static String BOOK_POSITION = "BOOK_POSITION";
    public final static String FONT_SIZE = "FONT_SIZE";

    @BindView(R.id.pages)
    ViewPager pagesView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    ActionMenuItemView menuItem;

    private LinkedHashMap<String, Integer> chapterPages;
    private DbHelper dbHelper;
    private Integer page;
    private Integer currentPage;
    private Integer bookPosition;
    private BookLoader bookLoader;
    private boolean didLoad;

    public static String currentBook;
    public static Integer pageCount;
    public static Integer fontSize;

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
        setFontSize();

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
                        toggleBookmark();
                        break;
                    case R.id.action_changeBook:
                        loadBooksList();
                        break;
                    case R.id.action_tabs:
                        openTabs();
                        break;
                    case R.id.action_goToPage:
                        goToPage();
                        break;
                    case R.id.action_changeFont:
                        changeFont();
                        break;

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
        linearLayout.addView(numberPicker, numPicerParams);

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

    private void changeFont() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Veličina slova");
        alert.setMessage("Nakon promjene sve označene stranice će biti obrisane");

        LinearLayout linear = new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);
        final TextView text = new TextView(this);
        text.setText(String.valueOf(fontSize));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        text.setPadding(10, 10, 10, 10);

        final SeekBar seekBar = new SeekBar(this);
        seekBar.setPadding(50,50,50,0);
        final int step = 1;
        final int max = 24;
        final int min = 16;

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (seekBar != null) {
                    seekBar.setMax((max - min) / step);
                    seekBar.setProgress(fontSize - 16);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = min + (progress * step);
                text.setText(String.valueOf(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        linear.addView(seekBar);
        linear.addView(text);

        alert.setView(linear);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Check if font size has changed
                if(fontSize != min + (seekBar.getProgress() * step))
                    dbHelper.deleteAllBookmarks(currentBook);
                Intent refresh = new Intent(BookActivity.this, BookActivity.class);
                refresh.putExtra(BookActivity.FONT_SIZE, min + (seekBar.getProgress() * step));
                startActivity(refresh);
            }
        });
        alert.setNegativeButton("Poništi", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        alert.show();
    }

    @SuppressLint("RestrictedApi")
    private void toggleBookmark() {
        menuItem = findViewById(R.id.action_bookmark);

        String currentChapter = "";
        for (int i = 0; i < chapterPages.size() - 1; i++) {
            if (currentPage >= (new ArrayList<Integer>(chapterPages.values())).get(i) && currentPage < (new ArrayList<Integer>(chapterPages.values())).get(i + 1)) {
                currentChapter = (new ArrayList<String>(chapterPages.keySet())).get(i);
                break;
            }
        }

        if (!dbHelper.bookmarkExists(currentPage.toString(), currentBook)) {
            dbHelper.insertBookmarkData(currentPage, currentChapter, currentBook);

            Toast.makeText(this, "Stranica označena", Toast.LENGTH_SHORT).show();
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_white));
        } else {
            dbHelper.deleteBookmarkData(currentPage.toString(), currentBook);

            Toast.makeText(this, "Oznaka uklonjena", Toast.LENGTH_SHORT).show();
            menuItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border_white));
        }

    }

    private void setCurrentBook() {
        bookPosition = getIntent().getIntExtra(BOOK_POSITION, -1);

        if (bookPosition == -1)
            return;

        switch (bookPosition) {
            case 0:
                currentBook = "Knjiga o nepravednim ljudima";
                break;
            case 1:
                currentBook = "Najstrpljiviji zatvorenik";
                break;
            case 2:
                currentBook = "Vodič kroz život";
                break;
        }
    }

    private void setFontSize() {
        fontSize = getIntent().getIntExtra(FONT_SIZE, 18);
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
                new LoadBookAsync().execute(this);
            }
        });

        pagesView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getSupportActionBar().setSubtitle("Stranica " + (position + 1));
                currentPage = position;
                menuItem = findViewById(R.id.action_bookmark);

                if (menuItem != null) {
                    if (!dbHelper.bookmarkExists(currentPage.toString(), currentBook)) {
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


    private class LoadBookAsync extends AsyncTask<ViewTreeObserver.OnGlobalLayoutListener, Integer, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(final ViewTreeObserver.OnGlobalLayoutListener... onGlobalLayoutListeners) {
            if (didLoad)
                return null;

            final PageSplitter pageSplitter = new PageSplitter(pagesView.getWidth(), pagesView.getHeight(), 1, 5);

            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, fontSize, getResources().getDisplayMetrics()));

            Cursor result = dbHelper.getChapterData();
            if (result.getCount() != 0) {
                while (result.moveToNext()) {
                    if (result.getString(3).contentEquals(currentBook)) {
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
            didLoad = true;
            pageCount = pageSplitter.getCount();

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    pagesView.setAdapter(new TextPagerAdapter(getSupportFragmentManager(), pageSplitter.getPages()));
                    pagesView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListeners[0]);
                    pagesView.setCurrentItem(page);
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
