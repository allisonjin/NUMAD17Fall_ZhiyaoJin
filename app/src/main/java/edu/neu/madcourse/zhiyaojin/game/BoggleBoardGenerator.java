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
    private final List<int[]> boards;
    private final Random random;

    public BoggleBoardGenerator(DictionaryDBHelper dbHelper, int size) {
        this.size = size;
        int n = size * size;
        words = dbHelper.getWordsByLength(n);
        random = new Random();

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i;
        }
        boards = getALlLegalBoards(nums);
    }

    private <T> T randomElement(List<T> list) {
        int idx = random.nextInt(list.size());
        return list.get(idx);
    }

    public char[] getRandomBoard() {
        String word = randomElement(words);
        Log.i("word", word);
        int[] numBoard = randomElement(boards);
        char[] board = new char[numBoard.length];
        for (int i = 0; i < numBoard.length; i++) {
            board[i] = word.charAt(numBoard[i]);
        }
        return board;
    }


    private List<int[]> getALlLegalBoards(int[] nums) {
        List<int[]> boards = new ArrayList<>();
        int[][] board = new int[size][size];
        for (int[] row : board) {
            Arrays.fill(row, -1);
        }
        int x = random.nextInt(size);
        int y = random.nextInt(size);
        dfs(nums, x, y, 0, board, boards);
        return boards;
    }

    // nums stores the indexes of letters in a word
    private void dfs(int[] nums, int x, int y, int index, int[][] board,
                     List<int[]> boards) {
        if (x < 0 || x >= size || y < 0 || y >= size || board[x][y] != -1) {
            return;
        }
        board[x][y] = nums[index];
        if (index == nums.length - 1) {
            // deep copy
            int[] newBoard = new int[size * size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(board[i], 0, newBoard, size * i, size);
            }
            boards.add(newBoard);
            board[x][y] = -1;
            return;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    dfs(nums, x + i, y + j, index + 1, board, boards);
                }
            }
        }
        board[x][y] = -1;
    }

}
