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
        
        <script src="./javascript/ttt.js" type="text/javascript"></script>
    </head>
    <body>

        <script type="text/javascript">console.log(ttt);</script>
        <div id="gameControls">
            <button onclick="ttt.createGame('client');">New Game</button>
        </div>
        <div id="moveControls">
            <input type="number" min="0" max="2" />
            <input type="number" min="0" max="2" />
            <button>Move</button>
        </div>
    </body>
</html>
