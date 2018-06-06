/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass2.s_16058989.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 *
 * @author Octet
 */
public class Board {
    
    public enum Player {
        NONE ("None", "_"),
        COMPUTER ("Computer", "O"),
        CLIENT ("Player", "X");
        
        private final String name;
        private final String piece;
        Player(String name, String piece) {
            this.name = name;
            this.piece = piece;
        }
        
        public String getName() {
            return name;
        }
        
        public String getPiece() {
            return piece;
        }
    }
    
    //Variables
    final int SIZE = 3;
    private Player board[][] = new Player[SIZE][SIZE];
    
    private Player turn = Player.NONE;
    private Player winner = Player.NONE;
    
   
    /**
     * Creates a new game instance.
     * Constructor.
     */
    public Board() {
        for(Player[] row : board) {
            Arrays.fill(row, Player.NONE);
        }
    }
    
    /**
     * Gets the winner of the game
     * @return the winner or Player.None
     */
    public Player getWinner() {
        return winner;
    }
    
    /**
     * Sets who's turn it is
     * @param turn some {@link}Player
     */
    public void setTurn(Player turn) {
        this.turn = turn;
    }
    
    /**
     * Get who's turn it is
     * @return a {@link}Player
     */
    public Player getTurn() {
        return turn;
    }
    
    
    /**
     * Returns an ArrayList containing all the possible moves [row,col]
     * @return
     */
    public ArrayList<Integer[]> getPossibleMoves() {
        ArrayList<Integer[]> possible = new ArrayList<Integer[]>();
        
        for(int row = 0; row < SIZE; row++) {
            for(int col = 0; col < SIZE; col++) {
                if(board[row][col] == Player.NONE) {
                    Integer[] grid = {row, col};
                    possible.add(grid);
                }
            }
        }
        
        return possible;
    }
    
    /**
     * Returns a String containing the current state of the board
     * @return a String consisting of _, X, or O
     */
    public String display() {
        String out = "";
        
        for(int row = 0; row < SIZE; row++) {
            for(int col = 0; col < SIZE; col++) {
                out += board[row][col].getPiece();
            }
            
            out += "\n";
        }
        return out;
    }
    
}
