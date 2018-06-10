/**
 * Author: Dylan Tonks
 * Student ID: 16058989
 * Assignment 2, Advanced Web Development (159.352)
 * 
 * 
 * This file provides functionality to the Tic Tac Toe game, communicating with
 * the Tomcat server.
 * 
 * Dependencies:
 * |- jQuery
 */

'use strict';

var ttt = (function() {

    /*
     * Variables
     * =========================================================================
     */

   var global = {
       url : {
           move : './ttt/move/',
           possibleMoves : './ttt/possiblemoves',
           state : './ttt/state',
           won : './ttt/won',
           ustart : './ttt/ustart',
           istart : './ttt/istart'
       },
       players : {
           client : 'client',
           computer : 'computer'
       },
       sessionId : null
   };
   
   

   /*
    * GLOBAL FUNCTIONS
    * ==========================================================================
    */
   /**
    * Initalization is called when the file is included.
    * @returns {undefined}
    */
   function init() {
       
       $(document).ready(function() {
           
           global.elem = {
                $moveControls : $('#moveControls'),
                $move_x : $('#moveControls .move_x'),
                $move_y : $('#moveControls .move_y'),
                $board : $('#gameBoard table'),
                $status : $('.status')
           };
       });
       
   }
   
   
   /**
    * Creates a new game for the client
    * @param {type} starter the player who should start
    * @returns {undefined}
    */
   function createGame(starter) {

        //This funciton is invoked when there is a response from the server.
        var onData = function response(data, status) {
            
            if(data.length !== 0) {
                global.sessionId = data.trim();
            }
            global.elem.$moveControls.show();
            return updateBoard();
        };

        if(starter === global.players.client) {
            sendRequest(global.url.istart, "", "POST", "", onData);
        }
        else if(starter === global.players.computer) {
            sendRequest(global.url.ustart, "", "POST", "", onData);
        }
    };
    
    
    /**
     * Checks if the move is valid, then sends the move.
     * @returns {undefined}
     */
    function makeMove() {
        var x = global.elem.$move_x.val();
        var y = global.elem.$move_y.val();
        
        //Is the entered move a valid possible move?
        sendRequest(global.url.possibleMoves, "", "GET", "", function(data, status) {
            if(status === 200) {
                
                if(data.includes(x + "," + y)) {
                    
                    //If it is, make the move
                    var url = global.url.move + "x" + x + "y" + y;
                    sendRequest(url, "", "POST", "", function(data, status) {
                        updateBoard();
                    });
                }
                else {
                    updateStatus("Sorry, but you cannot move here.");
                }
            }
        });
    }
    
    

    /*
     * PRIVATE FUNCTIONS
     * =========================================================================
     */
    /**
     * Sends a new request to the server.
     * @param {type} url the path to send the request
     * @param {type} type the type of request: POST or GET
     * @param {type} data the data to send
     * @param {type} response the function to return the response data to.
     * @returns {undefined}
     */
   function sendRequest(url, params, type = "POST", data = "", response = function(d, s){}) {
       updateStatus("");
       
       var xhttp = new XMLHttpRequest();
       
       xhttp.onreadystatechange = function() {
           if(this.status === 404) {
               updateStatus(":( We have lost your session. Start a new game.");
               return;
           }
           
           if(this.readyState === 4) {
               return response(this.responseText, this.status);
           }
           else if(this.readyState === 0) {
               updateStatus(":( There is a network problem. We cannot connect.");
           }
       };
       
       //If cookies disabled, encode in URL
       xhttp.open(type, 
            (global.sessionId !== null) ? 
            (url + ';jsessionid=' + global.sessionId + params) : 
            (url + params), 
        true);
       xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
       xhttp.send(data);
   }
   

    /**
     * Checks if the game has ended, and if so, who won?
     * @returns {Boolean}
     */
    function isGameover() {
        
        sendRequest(global.url.won, "", "GET", "", function(data, status) {
            if(status === 200) {
                
                switch(data.trim()) {
                    case "Player":
                        updateStatus("Hooray! You have won!")
                        break;
                    case "Computer":
                        updateStatus(":( You have lost!");
                        break;
                }
            }
        });
        return false;
    }
    
    
   /**
    * Updates the client's game board with the server's version.
    * @returns {undefined}
    */
   function updateBoard() {
       sendRequest(global.url.state, "?format=txt", "GET", "", function(data, status) {
           if(status !== 200) {
               updateStatus("There was a problem creating a new game.");
               return;
           }
           
           
           
           //Remove the last \n from the results, then split the data per line
           data = data.trim().split('\n');
           
           var newBoard = "";
           for(var line in data) {
               
               //Adds a row to the board
               newBoard += "<tr>";
               for(var value in data[line]) {
                   
                   //If the position is available,
                   if(data[line][value] === '_') {
                       
                       //Create a column containing the value
                       newBoard += "<td class='available'>" + data[line][value] + "</td>";
                   }
                   else {
                       newBoard += "<td>" + data[line][value] + "</td>";
                   }
                    
               }
               newBoard += "</tr>";
           }
           global.elem.$board.html(newBoard);
           isGameover();
       });
   }
   
   /**
    * Updates the status text for the client
    * @param {type} string the new status
    * @returns {undefined}
    */
   function updateStatus(string) {
       global.elem.$status.text(string);
   }
   
   
   
   /*
    * Exports global functions & variables
    * ==========================================================================
    */
   return {
       init: init(),
       global: global,
       createGame: createGame,
       makeMove: makeMove
   };
}());