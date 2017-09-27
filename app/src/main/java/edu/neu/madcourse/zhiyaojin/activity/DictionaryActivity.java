package edu.neu.madcourse.zhiyaojin.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.neu.madcourse.zhiyaojin.R;
import edu.neu.madcourse.zhiyaojin.dictionary.DictionaryDBHelper;
import edu.neu.madcourse.zhiyaojin.fragment.AcknowledgmentDialogFragment;

public class DictionaryActivity extends AppCompatActivity {
    private static final int WORD_MIN_LENGTH = 3;

    private DictionaryDBHelper helper;

    private EditText searchEditText;
    private TextView enteredWordsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);
        getSupportActionBar().setTitle(R.string.dict_title);

        helper = new DictionaryDBHelper(getApplicationContext());
        searchEditText = (EditText)findViewById(R.id.dict_search);
        enteredWordsTextView = (TextView)findViewById(R.id.dict_entered_words);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String word = s.toString();
                if (s.length() >= WORD_MIN_LENGTH && helper.wordExists(word)) {
                    updateEnteredWords(word);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateEnteredWords(String word) {
        String text = word + "\n" + enteredWordsTextView.getText();
        enteredWordsTextView.setText(text);
    }

    public void clear(View view) {
        searchEditText.setText("");
        enteredWordsTextView.setText("");
    }

    public void displayAcknowledgment(View view) {
        FragmentManager fm = getSupportFragmentManager();
        AcknowledgmentDialogFragment dialog = new AcknowledgmentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("source", getString(R.string.source_assignment2));
        dialog.setArguments(bundle);
        dialog.setRetainInstance(true);
        dialog.show(fm, "acknowledgment");
    }
}
