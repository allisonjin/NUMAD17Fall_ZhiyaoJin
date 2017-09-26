package edu.neu.madcourse.zhiyaojin.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.zhiyaojin.R;

/**
 * Created by allisonjin on 9/25/17.
 */

public class DictionaryDB {
    private static final String DB_NAME = "dictionary";
    private static final int DB_VERSION = 2;
//    private static final String FTS_VIRTUAL_TABLE = "FTSdictionary";
    private static final String TABLE_NAME = "dict";

    private static final String WORD = "word";

//    private final DictionaryDBOpenHelper dbOpenHelper;
    private final SQLiteDatabase dictionaryDB;

    public DictionaryDB(Context context) {
//        dbOpenHelper = new DictionaryDBOpenHelper(context);
        DictionaryDBOpenHelper dbOpenHelper = new DictionaryDBOpenHelper(context);
        dictionaryDB = dbOpenHelper.getReadableDatabase();
    }

    public boolean wordExists(String word) {
        return query(word).getCount() > 0;
    }

    private Cursor query(String word) {
        String[] columns = new String[]{WORD};
        String selection = WORD + "=?";
        String[] selectionArgs = new String[]{word};
//        return dbOpenHelper.getReadableDatabase().query(FTS_VIRTUAL_TABLE,
//                columns, selection, selectionArgs, null, null, null);
        return dictionaryDB.query(TABLE_NAME,
                columns, selection, selectionArgs, null, null, null);
    }

    private class DictionaryDBOpenHelper extends SQLiteOpenHelper {
        private final Context helperContext;
        private SQLiteDatabase sqLiteDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" + WORD + ");";

        DictionaryDBOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            helperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            sqLiteDatabase = db;
            sqLiteDatabase.execSQL(FTS_TABLE_CREATE);
            loadDictionary();
        }

        private void loadDictionary() {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        loadWords();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }

        private void loadWords() throws IOException {
            Log.i("db", "loading");
            InputStream inputStream = helperContext.getResources().openRawResource(R.raw.wordlist);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> words = new ArrayList<>();

            try {
                String line;
                while((line = reader.readLine()) != null) {
                    words.add(line.trim());
                }
            } finally {
                reader.close();
            }
            addWords(words);
        }

        private void addWords(List<String> words) {
            ContentValues contentValues = new ContentValues();
            sqLiteDatabase.beginTransaction();
            for (String word : words) {
                contentValues.put(WORD, word);
                sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            }
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();
        }

        private void addWord(String word) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WORD, word);
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
