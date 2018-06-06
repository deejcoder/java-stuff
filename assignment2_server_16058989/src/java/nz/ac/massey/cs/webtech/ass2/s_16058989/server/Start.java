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
@WebServlet(name = "Start", urlPatterns = {"/ttt/istart", "/ttt/ustart"})
public class Start extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException 
    {
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
        
            String[] URI = request.getRequestURI().split("/");
            switch(URI[URI.length-1]) {
                case "ustart": {

                    //TODO: possibly need to check if the session already exists,
                    if(createGame(request, Board.Player.COMPUTER)) {
                        out.println("sucess");
                    }
                    else {
                        out.println("game in-progress");
                    }
                    break;
                }
                case "istart": {

                    createGame(request, Board.Player.CLIENT);
                    break;
                }
                default: {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    break;
                }
            }
        }
    }
    /*
    TODO: method that creates session, then istart: player starts, ustart: computer starts 
    (just triggers if it should wait for a response, or wait for the server to move).
    */
    
    /**
     * Creates a new game instance.
     * @param request the original request sent from the client's browser
     * @param starter the player that should start (CLIENT or COMPUTER).
     * @return boolean indicating if the game was created.
     */
    public boolean createGame(HttpServletRequest request, Board.Player starter) {
        
        //Create a new session for the client
        HttpSession session = request.getSession(true);

        
        //TODO: add support for if client browser rejects cookies
        if(session.isNew()) {
            
            //Create a new board for the client
            Board board = new Board();
            board.setTurn(starter);
            
            if(starter == Board.Player.COMPUTER) {
                //do computer's move, maybe
            }
            
            //Map the board to the session
            session.setAttribute("board", board);
            return true;
        }
        return false;
    }
}
