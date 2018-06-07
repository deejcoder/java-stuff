/**
 * Author: Dylan Tonks
 * Student ID: 16058989
 * Assignment 2, Advanced Web Development (159.352)
 * 
 * 
 * This file provides functionality to the Tic Tac Toe game, communicating with
 * the Tomcat server.
 */

'use strict';

var ttt = (function() {

    /*
     * Variables
     * =========================================================================
     */
    /**
     * Global variables/constants
     * @type type
     */
   var global = {
       url : {
           move : './ttt/move/',
           state : './ttt/state',
           ustart : './ttt/ustart',
           istart : './ttt/istart'
       },
       players : {
           client : 'client',
           computer : 'computer'
       },
       input : {
           move_x : '#moveControls .move_x',
           move_y : '#moveControls .move_y'
       }
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
       //createGame("computer");
   }
   
   
   /**
    * Creates a new game for the client
    * @param {type} starter the player who should start
    * @returns {undefined}
    */
   function createGame(starter) {

        var onData = function response(data) {
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
   function sendRequest(url, type = "POST", data = "", response = function(d){}) {
       
       var xhttp = new XMLHttpRequest();
       
       xhttp.onreadystatechange = function() {
           if(this.readyState === 4 && this.status === 200) {
               return response(this.responseText);
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
       sendRequest(global.url.state, "GET", "", function(data) {
           document.write(data);
       });
   }
   
   
   
   
   /*
    * Exports global functions & variables
    * ==========================================================================
    */
   return {
       init: init(),
       global: global,
       createGame: createGame
   };
}());