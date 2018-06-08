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
            
        
        String[] split = request.getRequestURI().split(";")[0].split("/");
        String URI = split[split.length-1];

        switch(URI) {
            case "ustart": 
                createGame(request, response, Board.Player.COMPUTER); 
                break;
            case "istart": 
                createGame(request, response, Board.Player.CLIENT); 
                break;
            default: 
                response.sendError(HttpServletResponse.SC_BAD_REQUEST); 
                return;
        }

    }
    /*
    TODO: method that creates session, then istart: player starts, ustart: computer starts 
    (just triggers if it should wait for a response, or wait for the server to move).
    */
    
    /**
     * Creates a new game instance.
     * @param request the original request sent from the client's browser
     * @param response the response
     * @param starter the player that should start (CLIENT or COMPUTER).
     * @return boolean indicating if the game was created.
     * @throws java.io.IOException
     */
    public boolean createGame(HttpServletRequest request, HttpServletResponse response, Board.Player starter)
        throws IOException
    {
        
        //Create/Overwrite a new session for the client
        HttpSession session = request.getSession(true);

        if(!session.isNew()) {
            session.invalidate();
            session = request.getSession(true);
        }
            
        //Create a new board for the client
        Board board = new Board();
        board.setTurn(starter);

        if(starter == Board.Player.COMPUTER) {
            board.moveComputer();
        }

        //Map the board to the session
        session.setAttribute("board", board);
        
        //If cookies are disabled, reply with the session ID
        try (PrintWriter out = response.getWriter()) {
            if(!response.encodeURL("").isEmpty()) {
                out.println(session.getId());
            }
            
        }
        return true;
    }
}
