/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass2.s_16058989.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
        ArrayList<Vector> possible = new ArrayList<>();
        
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
     * Returns a PNG image of the game board
     * @param png
     * @param out
     * @param response
     * @return
     * @throws IOException 
     */
    public String display(boolean png, ServletOutputStream out, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");

        String text = display();
        
        BufferedImage image = new BufferedImage(50, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 20);
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        String[] lines = text.split("\n");
        int count = 1;
        for(String line : lines) {
            g2d.drawString(line, 0, count*25);
            count++;
        }
        
        g2d.dispose();
        
        javax.imageio.ImageIO.write(image, "png", out);
        return "";
    }
    
    
    /**
     * Allows a player to move, given x & y values.
     * @param x
     * @param y
     * @return true if the move was accepted, else false.
     */
    public boolean movePlayer(int x, int y) {
        /*
            TODO: check if gameover, if it is DO NOT move (checkWinner())
        */
        if(checkWinner()) {
            return false;
        }
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
                turn = Player.COMPUTER;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Triggers the computer to make a random move.
     * @return true if the computer is successful at making a move
     */
    public boolean moveComputer() {
        //TODO: check if gameover, if it is do NOT move
        if(checkWinner()) {
            return false;
        }
        //Get a list of all possible moves
        ArrayList<Vector> possible = getPossibleMoves();
        
        if(possible.isEmpty()) {
            return false;
        }
        
        //Choose a random move
        Random random = new Random();
        int randomInt = random.nextInt(possible.size());
        Vector vector = possible.get(randomInt);
        
        //Update the board
        board[vector.x][vector.y] = Player.COMPUTER;
        turn = Player.CLIENT;
        checkWinner(); //has computer won?
        return true;
        
    }
    
    public boolean checkWinner() {
        if(getWinner() != Player.NONE) {
            return true;
        }
        
        
        //CONDITION 1: player as occupied a diagonal
        if(board[0][0].equals(board[1][1])
                && board[0][0].equals(board[2][2])
                && !board[0][0].equals(Player.NONE)) {
            winner = board[0][0];
            return true;
        }
        
        if(board[0][2].equals(board[1][1])
                && board[0][2].equals(board[2][0])
                && !board[0][2].equals(Player.NONE)) {
            winner = board[0][2];
            return true;
        }
        //====
        for(int i = 0; i < SIZE; i++) {
            
            //CONDITION 2: player as occupied an entire row
            if(board[i][0].equals(board[i][1]) 
                    && board[i][0].equals(board[i][2])
                    && !board[i][0].equals(Player.NONE)) {
                winner = board[i][0];
                return true;
            }
            //====
            
            //CONDITION 3: player as occupied entire column
            if(board[0][i].equals(board[1][i])
                    && board[0][i].equals(board[2][i])
                    && !board[0][i].equals(Player.NONE)) {
                winner = board[0][i];
                return true;
            }
            //====
              
        }
        
        //The winner will be the last opposite of 'turn' variable, !turn
        return false;
    }
    
}
