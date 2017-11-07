/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4;

import ch.hslu.ai.connect4.Player;
/**
 *
 * @author furrer.markus
 */
public class ColumnPlayer extends Player{
    
    public ColumnPlayer(String name){
        super(name);
    }
    
    @Override
    public int play(char[][] board) {
        int column = -1;

        do {
            column++;
        } while (board[column][0] != '-');
        return column;
        
    }
}
