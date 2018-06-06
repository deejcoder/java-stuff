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
@WebServlet(name = "PossibleMoves", urlPatterns = {"/ttt/possiblemoves"})
public class PossibleMoves extends HttpServlet {


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
        
        //Get the session, if the client has one
        HttpSession session = request.getSession(false);
        if(session == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        //Get the board corresponding to the session
        Board board = (Board) session.getAttribute("board");
        try (PrintWriter out = response.getWriter()) {
            
            //Get all the available possibles, return them
            for(Integer[] pos : board.getPossibleMoves()) {
                out.println(pos[0] + "," + pos[1]);

            }
        }
    }

}
