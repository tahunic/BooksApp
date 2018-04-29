package com.example.nihad.booksapp;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * Created by nihad on 27/01/2018.
 */

public class BookLoader {
    DbHelper dbHelper;
    Context context;

    public BookLoader(Context context) {
        this.dbHelper = new DbHelper(context);
        this.context = context;
    }

    public void insertData() {

        if (dbHelper.isDatabaseEmpty()) {

            JSONObject json = null;
            try {
                json = new JSONObject(readJSONFromAsset("books.json"));
                String bookName = "Knjiga o nepravednim ljudima";
                int length = json.getJSONArray(bookName).length();
                Iterator<String> keys;

                for (int i = 0; i < length; i++) {
                    keys  = json.getJSONArray(bookName).getJSONObject(i).keys();

                    for (String key; keys.hasNext(); ) {
                        key = keys.next();

                        String content = json.getJSONArray(bookName).getJSONObject(i).getString(key);
                        dbHelper.insertChapterData(key,  content, bookName);
                    }
                }

                json = new JSONObject(readJSONFromAsset("books.json"));
                bookName = "Najstrpljiviji zatvorenik";
                length = json.getJSONArray(bookName).length();

                for (int i = 0; i < length; i++) {
                    keys  = json.getJSONArray(bookName).getJSONObject(i).keys();

                    for (String key; keys.hasNext(); ) {
                        key = keys.next();

                        String content = json.getJSONArray(bookName).getJSONObject(i).getString(key);
                        dbHelper.insertChapterData(key,  content, bookName);
                    }
                }

                json = new JSONObject(readJSONFromAsset("books.json"));
                bookName = "Vodič kroz život";
                length = json.getJSONArray(bookName).length();

                for (int i = 0; i < length; i++) {
                    keys  = json.getJSONArray(bookName).getJSONObject(i).keys();

                    for (String key; keys.hasNext(); ) {
                        key = keys.next();

                        String content = json.getJSONArray(bookName).getJSONObject(i).getString(key);
                        dbHelper.insertChapterData(key,  content, bookName);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public String readJSONFromAsset(String name) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
