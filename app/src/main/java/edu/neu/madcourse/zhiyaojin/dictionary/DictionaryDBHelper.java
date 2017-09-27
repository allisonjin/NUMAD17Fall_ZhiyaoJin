package edu.neu.madcourse.zhiyaojin.dictionary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.zhiyaojin.BuildConfig;
import edu.neu.madcourse.zhiyaojin.R;

/**
 * Created by allisonjin on 9/25/17.
 */

public class DictionaryDBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DictionaryDBHelper";

    private static final String DB_NAME = "dictionary.db";
    private static final String DB_PATH = "/data/data/"
            + BuildConfig.APPLICATION_ID + "/databases/";
    private static final int DB_VERSION = 2;
    private static final String TABLE_NAME = "dict";
    private static final String WORD = "word";
    private static final String INDEX = TABLE_NAME + "_" + WORD;

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" + WORD + ");";
    private static final String CREATE_INDEX =
            "CREATE INDEX " + INDEX + " ON " + TABLE_NAME + " (" + WORD + ");";

    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public DictionaryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
        openOrCopyDatabase();
    }

    private void openOrCopyDatabase() {
        String dbFilePath = DB_PATH + DB_NAME;
        File dbFile = new File(dbFilePath);
        if (!dbFile.exists()) {
            File dbPath = new File(DB_PATH);
            dbPath.mkdirs();
            copyDatabase(dbFile);
        }
        mSQLiteDatabase = SQLiteDatabase.openDatabase(dbFile.getPath(),
                null, SQLiteDatabase.OPEN_READONLY);
    }

    private void copyDatabase(File dbFile) {
        InputStream is = null;
        OutputStream os = null;
        try {
            try {
                is = mContext.getAssets().open(DB_NAME);
            } catch (IOException e) {
                createDBFromDictionary();
            }
            os = new FileOutputStream(dbFile);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                os.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) os.close();
                if (is != null) is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createDBFromDictionary() {
        mSQLiteDatabase = this.getReadableDatabase();
        mSQLiteDatabase.execSQL(CREATE_TABLE);
        mSQLiteDatabase.execSQL(CREATE_INDEX);
        loadDictionary();
    }

    private void loadDictionary() {
        Log.i(TAG, "Loading dictionary file...");
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.wordlist);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> words = new ArrayList<>();

        try {
            String line;
            while((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        addWords(words);
    }

    private void addWords(List<String> words) {
        ContentValues contentValues = new ContentValues();
        mSQLiteDatabase.beginTransaction();
        for (String word : words) {
            contentValues.put(WORD, word);
            mSQLiteDatabase.insert(TABLE_NAME, null, contentValues);
        }
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    public boolean wordExists(String word) {
        return query(word).getCount() > 0;
    }

    private Cursor query(String word) {
        String[] columns = new String[]{WORD};
        String selection = WORD + "=?";
        String[] selectionArgs = new String[]{word};
        return mSQLiteDatabase.query(TABLE_NAME,
                columns, selection, selectionArgs, null, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
