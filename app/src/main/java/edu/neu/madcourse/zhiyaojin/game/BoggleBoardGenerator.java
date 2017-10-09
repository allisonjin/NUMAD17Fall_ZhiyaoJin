package edu.neu.madcourse.zhiyaojin.game;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.neu.madcourse.zhiyaojin.dictionary.DictionaryDBHelper;

public class BoggleBoardGenerator {

    private final int size;
    private final List<String> words;
    private final Random random;

    public BoggleBoardGenerator(DictionaryDBHelper dbHelper, int size) {
        this.size = size;
        words = dbHelper.getWordsByLength(size * size);
        random = new Random();
    }

    private <T> T randomElement(List<T> list) {
        int idx = random.nextInt(list.size());
        return list.get(idx);
    }

    public char[] getRandomBoard() {
        String word = randomElement(words);
        Log.i("word", word);
        List<char[]> boards = getAllBoards(word);
        char[] board = randomElement(boards);
        return board;
    }

    private List<char[]> getAllBoards(String word) {
        List<char[]> boards = new ArrayList<>();
        char[][] board = new char[size][size];
        for (char[] chs : board) {
            Arrays.fill(chs, '#');
        }
        int x = random.nextInt(size);
        int y = random.nextInt(size);
        dps(word, x, y, 0, board, boards);
        return boards;
    }

    private void dps(String word, int x, int y, int index, char[][] board,
                        List<char[]> boards) {
        if (x < 0 || x >= size || y < 0 || y >= size || board[x][y] != '#') {
            return;
        }
        board[x][y] = word.charAt(index);
        if (index == word.length() - 1) {
            // deep copy
            char[] newBoard = new char[size * size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(board[i], 0, newBoard, size * i, size);
            }
            boards.add(newBoard);
            board[x][y] = '#';
            return;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    dps(word, x + i, y + j, index + 1, board, boards);
                }
            }
        }
        board[x][y] = '#';
    }

}
