package edu.neu.madcourse.zhiyaojin.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import edu.neu.madcourse.zhiyaojin.R;

public class GameResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        getSupportActionBar().setTitle(R.string.game_result_title);
        ListView listView = (ListView)findViewById(R.id.words_found);
        TextView scoreTextView = (TextView)findViewById(R.id.final_score);
        Button replayBtn = (Button)findViewById(R.id.replay_btn);
        final Button backToGameMenuBtn = (Button)findViewById(R.id.game_menu_btn);

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameResultActivity.this, ScroggleActivity.class));
            }
        });

        backToGameMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToGameMenu();
            }
        });

        Intent intent = getIntent();

        List<String> wordList = intent.getStringArrayListExtra("wordList");
        String[] words = wordList.toArray(new String[0]);
        WordListAdapter wordListAdapter = new WordListAdapter(this, words);
        listView.setAdapter(wordListAdapter);

        int score = intent.getIntExtra("score", 0);
        scoreTextView.setText("Score: " + score);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToGameMenu();
        finish();
    }

    private void backToGameMenu() {
        startActivity(new Intent(this, GameMenuActivity.class));
    }

    private class WordListAdapter extends ArrayAdapter {

        private final Activity context;
        private final String[] words;

        public WordListAdapter(Activity context, String[] words) {
            super(context, R.layout.word_found_single, words);
            this.context = context;
            this.words = words;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.word_found_single, null, true);
            TextView textView = rowView.findViewById(R.id.word_found);
            textView.setText(words[position]);
            return rowView;
        }
    }
}
