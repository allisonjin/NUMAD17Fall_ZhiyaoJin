package edu.neu.madcourse.zhiyaojin.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import edu.neu.madcourse.zhiyaojin.R;

/**
 * Created by allisonjin on 9/25/17.
 */

public class DictionaryDB {
    private static final String DB_NAME = "dictionary";
    private static final int DB_VERSION = 2;
    private static final String FTS_VIRTUAL_TABLE = "FTSdictionary";

    private static final String WORD = "word";


    private final DictionaryDBOpenHelper dbOpenHelper;

    public DictionaryDB(Context context) {
        dbOpenHelper = new DictionaryDBOpenHelper(context);
    }

    public boolean wordExists(String word) {
        return query(word) != null;
    }

    public Cursor query(String word) {
        String[] columns = new String[]{WORD};
        String selection = WORD + "=?";
        String[] selectonArgs = new String[]{word};
        return dbOpenHelper.getReadableDatabase().query(FTS_VIRTUAL_TABLE,
                columns, selection, selectonArgs, null, null, null);
    }

    private class DictionaryDBOpenHelper extends SQLiteOpenHelper {
        private final Context helperContext;
        private SQLiteDatabase sqLiteDatabase;

        private static final String FTS_TABLE_CREATE =
                "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE + " USING fts3 (" + WORD + ");";

        DictionaryDBOpenHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            helperContext = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            sqLiteDatabase = db;
            sqLiteDatabase.execSQL(FTS_TABLE_CREATE);

        }

//        private void loadDictionary() {
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        loadWords();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }).start();
//        }

        private void loadDicionary() {
            InputStream inputStream = helperContext.getResources().openRawResource(R.raw.wordlist);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while((line = reader.readLine()) != null) {
                    addWord(line.trim());
                }
            } catch (IOException e) {
                throw new RuntimeException();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        private void addWord(String word) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WORD, word);
            sqLiteDatabase.insert(FTS_VIRTUAL_TABLE, null, contentValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
            onCreate(db);
        }
    }
}
