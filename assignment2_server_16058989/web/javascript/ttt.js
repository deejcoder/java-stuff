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
    /**
     * Global variables/constants
     */
   var global = {
       url : {
           move : './ttt/move/',
           state : './ttt/state',
           won : './ttt/won',
           ustart : './ttt/ustart',
           istart : './ttt/istart'
       },
       players : {
           client : 'client',
           computer : 'computer'
       },
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
                $move_x : $('#moveControls .move_x'),
                $move_y : $('#moveControls .move_y'),
                $board : $('#gameBoard')
           };
       });
           
       
       
   }
   
   
   /**
    * Creates a new game for the client
    * @param {type} starter the player who should start
    * @returns {undefined}
    */
   function createGame(starter) {

        var onData = function response(data, status) {
            return updateBoard();
        };

        if(starter === global.players.client) {
            sendRequest(global.url.istart, "POST", "", onData);
        }
        else if(starter === global.players.computer) {
            sendRequest(global.url.ustart, "POST", "", onData);
        }
    };
    
    
    function makeMove() {
        var x = global.elem.$move_x.val();
        var y = global.elem.$move_y.val();
        
        var url = global.url.move + "x" + x + "y" + y;
        
        sendRequest(url, "POST", "", function(data, status) {
            if(status === 400) {
                console.log("Oh dear! Invalid move, try again.");
            }
        });
        
        updateBoard();
        isGameover();
    }
    
    
    function isGameover() {
        sendRequest(global.url.won, "GET", "", function(data, status) {
            if(status === 200) {
                console.log(data);
                
                switch(data.trim()) {
                    case "Player":
                        alert("You have won!");
                        break;
                    case "Computer":
                        alert("Oh no! You have lost!");
                        break;
                }
            }
        });
        return false;
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
   function sendRequest(url, type = "POST", data = "", response = function(d, s){}) {
       
       var xhttp = new XMLHttpRequest();
       
       xhttp.onreadystatechange = function() {
           if(this.readyState === 4) {
               return response(this.responseText, this.status);
           }
       };
       
       xhttp.open(type, url, true);
       xhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
       xhttp.send(data);
   }

   
   /**
    * Updates the client's game board with the server's version.
    * @returns {undefined}
    */
   function updateBoard() {
       sendRequest(global.url.state, "GET", "", function(data, status) {
           global.elem.$board.text(data);
       });
   }
   
   
   
   
   /*
    * Exports global functions & variables
    * ==========================================================================
    */
   return {
       init: init(),
       global: global,
       createGame: createGame,
       makeMove: makeMove,
       isGameover: isGameover,
   };
}());