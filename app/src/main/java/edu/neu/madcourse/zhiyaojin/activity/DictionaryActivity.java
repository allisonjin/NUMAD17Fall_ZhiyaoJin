package edu.neu.madcourse.zhiyaojin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.dictionary.DictionaryDB;

public class DictionaryActivity extends AppCompatActivity {
    private DictionaryDB dictionaryDB;

    private TextView searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        getSupportActionBar().setTitle(R.string.dict_title);

        dictionaryDB = new DictionaryDB(getApplicationContext());

        searchEditText = (TextView)findViewById(R.id.dict_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    Log.i("test", s.toString() + " exists: " + dictionaryDB.wordExists(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
