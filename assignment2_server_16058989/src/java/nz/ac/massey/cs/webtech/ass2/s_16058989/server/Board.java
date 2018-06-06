/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass2.s_16058989.server;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Octet
 */


public class Board {
    
    /**
     * Defines a Player
     */
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
    public ArrayList<Vector> getPossibleMoves() {
        ArrayList<Vector> possible = new ArrayList<Vector>();
        
        for(int row = 0; row < SIZE; row++) {
            for(int col = 0; col < SIZE; col++) {
                if(board[row][col] == Player.NONE) {
                    Vector vector = new Vector(row, col);
                    possible.add(vector);
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
    
    /**
     * Allows a player to move, given x & y values.
     * @param x
     * @param y
     * @return true if the move was accepted, else false.
     */
    public boolean movePlayer(int x, int y) {
        //Get all the possible moves
        ArrayList<Vector> possible = getPossibleMoves();
        
        //If there are possible moves,
        if(!possible.isEmpty()) {
        
            /*  Check if the move by the player is possible
                before giving the player the move
            */
            Vector vector = new Vector(x, y);
            
            if(possible.contains(vector)) {
                board[x][y] = Player.CLIENT;
                return true;
            }
        }
        return false;
    }
    
}
