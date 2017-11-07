/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import ch.hslu.ai.connect4.Player;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author furrer.markus
 */
public class IntelligentPlayer extends Player {
     
    private int depth = 1;
    /**
     * Constructor:
     *
     * @param name The name of this computer player
     */
    public IntelligentPlayer(String name) {
        super(name);

    }

    /**
     * The following method allows you to implement your own game intelligence.
     * At the moment, this is a dumb random number generator. The method must
     * return the column number where the computer player puts the next disc.
     * board[i][j] = cell content at position (i,j), i = column, j = row
     *
     * If board[i][j] = this.getSymbol(), the cell contains one of your discs If
     * board[i][j] = '-', the cell is empty Otherwise, the cell contains one of
     * your opponent's discs
     *
     * @param board The current game board
     * @return The columns number where you want to put your disc
     */
    @Override
    public int play(char[][] board) {
        char[][] b;

        HashMap<Integer, Integer> scores = new HashMap<>();
        for (int i = 0; i <= 6; ++i) {

       

            if (board[i][0] == '-') {
                b = this.addSymbol(board, i, this.getSymbol());
                scores.put(i, minMax(b, i, '#'));
            }
        }
       
        return getMaxKey(scores);
    }

    private char[][] addSymbol(char[][] board, int column, char symbol) {
        char[][] b = new char[board.length][board[0].length];
        for (int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[0].length; ++j) {
                b[i][j] = board[i][j];
            }
        }
        for (int i = 5; i >= 0; --i) {
            if (b[column][i] == '-') {
                b[column][i] = symbol;
                return b;
            }
        }
        return b;
    }

    private int availableColumns(char[][] board) {
        int count = 0;
        for (int i = 0; i <= 6; ++i) {
            if (board[i][0] == '-') {
                ++count;
            }
        }
        return count;
    }

    private boolean boardIsFull(char[][] board) {

        return availableColumns(board) == 0;
    } 

    private int minMax(char[][] board, int column, char symbol) {
       //Terminal node
        if (boardIsFull(board) || depth == 3) {

            return getBoardScores(board);
        }
        //Enemy node
        else if (depth % 2 == 1) {
            ++depth;
            int[] results = new int[7];
            for (int i = 0; i < results.length; ++i) {

                char[][] b;
                if (board[i][0] == '-') {
                    
                    b = addSymbol(board, i, '#');
                    results[i] = minMax(b, i, '#');
                } else {
                    results[i] = Integer.MAX_VALUE;
                }
            }
            --depth;
            return Arrays.stream(results).min().getAsInt();
        }
        //My node
        else {
            ++depth;
            int[] results = new int[7];
            for (int i = 0; i < results.length; ++i) {

                char[][] b;
                if (board[i][0] == '-') {
                   
                    b = addSymbol(board, i, this.getSymbol());
                    results[i] = minMax(b, i, this.getSymbol());
                } else {
                    results[i] = Integer.MIN_VALUE;
                }
            }
            --depth;
            return Arrays.stream(results).max().getAsInt();
        }
    }

    private int getMaxKey(HashMap<Integer, Integer> scores) {

        Set keys = scores.keySet();
        Iterator keyIterator = keys.iterator();
        Collection<Integer> values = scores.values();
        int key;
        int maxKey = -1;
        int maxValue = Integer.MIN_VALUE;
        for (Integer val : values) {
            key = (int) keyIterator.next();
            if (val > maxValue) {
                maxKey = key;
                maxValue = val;
            }
        }
        return maxKey;
    }

    private int getMinKey(HashMap<Integer, Integer> scores) {

        Set keys = scores.keySet();
        Iterator keyIterator = keys.iterator();
        Collection<Integer> values = scores.values();
        int key = -1;
        int minValue = Integer.MAX_VALUE;
        for (Integer val : values) {
            if (val < minValue) {
                key = (int) keyIterator.next();
                minValue = val;
            }
        }
        return key;
    }

    private int getBoardScores(char[][] board) {
        int scores = 0;

        for (int startingY = 0; startingY <= 5; ++startingY) {
            scores += rightDownDiag(board, startingY);
            scores += rightUpDiag(board, startingY);
            scores += leftDownDiag(board, startingY);
            scores += leftUpDiag(board, startingY);
            scores += horizontal(board, startingY);
        }
        for (int startingX = 0; startingX <= 6; ++startingX) {
            scores += vertical(board, startingX);
        }
        return scores;
    }

    private int rightDownDiag(char[][] board, int startPoint) {

        int i = 0;
        int j = startPoint;
        ArrayList<Character> fieldOfFour = new ArrayList<>();
        int scores = 0;

        while (j + 3 <= 5) {

            for (int counter = 0; counter <= 3; ++counter) {
                fieldOfFour.add(board[i + counter][j + counter]);
            }
            scores += getScoresOfFieldOfFour(fieldOfFour);
            fieldOfFour.clear();
            ++j;
            ++i;
        }
        return scores;
    }

    private int rightUpDiag(char[][] board, int startPoint) {

        int i = 0;
        int j = startPoint;
        ArrayList<Character> fieldOfFour = new ArrayList<>();
        int scores = 0;

        while (j - 3 >= 0) {

            for (int counter = 0; counter <= 3; ++counter) {
                fieldOfFour.add(board[i + counter][j - counter]);
            }
            scores += getScoresOfFieldOfFour(fieldOfFour);
            fieldOfFour.clear();
            --j;
            ++i;
        }
        return scores;
    }

    private int leftUpDiag(char[][] board, int startPoint) {

        int i = 6;
        int j = startPoint;
        ArrayList<Character> fieldOfFour = new ArrayList<>();
        int scores = 0;

        while (j - 3 >= 0) {

            for (int counter = 0; counter <= 3; ++counter) {
                fieldOfFour.add(board[i - counter][j - counter]);
            }
            scores += getScoresOfFieldOfFour(fieldOfFour);
            fieldOfFour.clear();
            --j;
            --i;
        }
        return scores;
    }

    private int leftDownDiag(char[][] board, int startPoint) {

        int i = 6;
        int j = startPoint;
        ArrayList<Character> fieldOfFour = new ArrayList<>();
        int scores = 0;

        while (j + 3 <= 5) {

            for (int counter = 0; counter <= 3; ++counter) {
                fieldOfFour.add(board[i - counter][j + counter]);
            }
            scores += getScoresOfFieldOfFour(fieldOfFour);
            fieldOfFour.clear();
            ++j;
            --i;
        }
        return scores;
    }

    private int horizontal(char[][] board, int startPoint) {

        int i = 0;
        int j = startPoint;
        ArrayList<Character> fieldOfFour = new ArrayList<>();
        int scores = 0;

        while (i + 3 <= 6) {

            for (int counter = 0; counter <= 3; ++counter) {
                fieldOfFour.add(board[i + counter][j]);
            }
            scores += getScoresOfFieldOfFour(fieldOfFour);
            fieldOfFour.clear();
            ++i;
        }
        return scores;
    }

    private int vertical(char[][] board, int startPoint) {

        int i = startPoint;
        int j = 0;
        ArrayList<Character> fieldOfFour = new ArrayList<>();
        int scores = 0;

        while (j + 3 <= 5) {

            for (int counter = 0; counter <= 3; ++counter) {
                fieldOfFour.add(board[i][j + counter]);
            }
            scores += getScoresOfFieldOfFour(fieldOfFour);
            fieldOfFour.clear();
            ++j;
        }
        return scores;
    }

    private int getScoresOfFieldOfFour(ArrayList<Character> fieldOfFour) {
        //only a is in the field
        if (Collections.frequency(fieldOfFour, this.getSymbol()) == 4) {
            return 100;
        } //a & _ is in the field
        else if (Collections.frequency(fieldOfFour, this.getSymbol())
                + Collections.frequency(fieldOfFour, '-') == 4) {
            return 20;
        } //a & b is in the field
        else if (Collections.frequency(fieldOfFour, this.getSymbol()) >= 1
                && Collections.frequency(fieldOfFour, this.getSymbol())
                + Collections.frequency(fieldOfFour, '-') < 4) {
            return 0;
        } //only _ is in the field
        else if (Collections.frequency(fieldOfFour, '-') == 4) {
            return 0;
        } //b & _ is in the field
        else if (Collections.frequency(fieldOfFour, this.getSymbol()) == 0
                && Collections.frequency(fieldOfFour, '-') < 4
                && Collections.frequency(fieldOfFour, '-') > 0) {
            return -20;
        } //only b is in the field
        else if (Collections.frequency(fieldOfFour, this.getSymbol()) == 0
                && Collections.frequency(fieldOfFour, '-') == 0) {
            return -1000;
        }
        return 0;
    }
}
