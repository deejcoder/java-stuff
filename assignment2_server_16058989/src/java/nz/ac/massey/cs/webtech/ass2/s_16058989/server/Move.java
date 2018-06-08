/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.ac.massey.cs.webtech.ass2.s_16058989.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
@WebServlet(name = "Move", urlPatterns = {"/ttt/move/*"})
public class Move extends HttpServlet {


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //Check if the client has a session
        HttpSession session = request.getSession();
        if(session == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        //Get the client's board
        Board board = (Board) session.getAttribute("board");
        
        
        //Make sure the URL pattern is valid. That is, */x{0-9}y{0-9}
        Pattern pattern = Pattern.compile("^.{0,}\\/x(\\d{1})y(\\d{1}).{0,}$");
        Matcher matcher = pattern.matcher(request.getRequestURI());
        
        if(matcher.find()) {
            //Extract the x & y values (assured decimals by regex)
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            
            if(board.movePlayer(x, y)) {
                board.moveComputer();
                return;
            }

        }
        //Invalid request or bad move
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Performs a move, posted by the client.";
    }// </editor-fold>

}
