/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass2.s_16058989.server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Octet
 */
@WebServlet(name = "Winner", urlPatterns = {"/ttt/won"})
public class Winner extends HttpServlet {



    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        //If there is no existing session
        if(session == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        response.setContentType("text/plain");
        
        Board board;
        try (PrintWriter out = response.getWriter()) {
            try {
                
                //Will throw ClassCastException if cannot be casted to a Board
                board = (Board) session.getAttribute("board");
                
                //Who won?
                switch(board.getWinner()) {
                    //If the client, or the computer has won...
                    case CLIENT:
                    case COMPUTER: {
                        out.println(board.getWinner().getName());
                        break;
                    }
                    //If draw, or no one has won...
                    case NONE: {
                        if(board.getPossibleMoves().isEmpty()) {
                            out.println("Draw");
                            break;
                        }
                            
                        //TODO: check if it's a draw: board.possibleMoves().length = 0
                        out.println(board.getWinner().getName());
                        break;
                    }
                }
            }

            catch(ClassCastException ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public String getServletInfo() {
        return "Informs the client of the winner of the game.";
    }// </editor-fold>

}
