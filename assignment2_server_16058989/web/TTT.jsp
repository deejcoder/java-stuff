<%-- 
    Document   : TTT
    Created on : 7/06/2018, 3:04:34 PM
    Author     : Octet
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- JSP creates sessions by default, let's disable this. --%>
<%@page session="false" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Tic Tac Toe</title>
        
        <link rel="stylesheet" type="text/css" href="./resources/ttt.css">
        
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <script src="./javascript/ttt.js" type="text/javascript"></script>
    </head>
    <body>

        <div id="gameBoard">
            <table>
            </table>
        </div>

        <div id="moveControls">
            <input type="number" placeholder="row" min="0" max="2" class="move_x" />
            <input type="number" placeholder="col" min="0" max="2" class="move_y" />
            <button onclick="ttt.makeMove();">Move</button>
        </div>
        
        <div id="gameControls">
            <p class="status"></p>
            <p><strong>Welcome to Tic Tac Toe!</strong><br>
                Want to start a new game? Who should start?:
            </p>
            <button onclick="ttt.createGame('client');">I want to start!</button>
            <button onclick="ttt.createGame('computer');">The computer can start!</button>
        </div>

    </body>
</html>
