var TicTacToe = (function() {
    'use strict';
    var DOM = {};

    DOM.settings = {
        apiURI: '/api/games/tictactoe'
    }

    function init() {

        $(document).ready(function() {

            DOM.board = {
                $board: $('#gameboard'),
                $winner: $(".winnerText"),
                $reset: $("#button_reset")
            }

            //Get the current board state
            console.log("DEBUG: getting board's initial state from application server.");
            sendRequest(
                {
                    action: "getstate"
                },
                function(data) {
                    if(Object.keys(data).length < 1) return false;
                    console.log("DEBUG: fetched initial state, setting up initial board...");

                    //Loop through the board (row, col)
                    for(var row = 0; row < data.moves.length; row++) {
                        for(var col = 0; col < data.moves[row].length; col++) {
                            //If no player has moved here
                            if(data.moves[row][col] == null) { continue; }

                            //Otherwise set the square to the player.
                            var player = ((data.moves[row][col] == "Player") ? "X" : "O");
                            setCellValue(row+1, col+1, player);


                        }
                    }
                    return true;
                }
            )

            console.log(DOM);
            OnClickEvent();
        });
    }

    /**
     * Sets a cell of the gameboard to a defined value
     * 
     * @param {row} row row to set
     * @param {col} column column to set
     * @param {value} value value to set
     */
    function setCellValue(row, col, value) {
        var $gridRow = DOM.board.$board.find("tr:nth-child(" + row + ")");
        $gridRow.find("td:nth-child(" + col + ")").text(value);
    
        console.log("DEBUG: set cell (" + row + "," + col + ") to " + value);
    }

    /**
     * Sends JSON to the game's application server.
     * 
     * @param {data} data JavaScript object to send
     * @param {responseFunction} responseFunction function that the response will be returned to, returns JS object as parameter
     */
    function sendRequest(data, responseFunction = function() { return true; }) {
        var xhttp = new XMLHttpRequest();

        xhttp.onreadystatechange = function() {

            //On HTTP 200 OK,
            if(this.readyState == 4 && this.status == 200) {
                //Transform the result into a JS object
                var jsonObj = JSON.parse(this.responseText);

                //If there is no data, exit
                //if(Object.keys(jsonObj).length < 1) return false;

                //Otherwise return the callback
                return responseFunction(jsonObj);
            }
        };
        xhttp.open("POST", DOM.settings.apiURI, true);
        xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhttp.send(JSON.stringify(data));
    }

    function OnClickEvent() {
        /**
         * When the user clicks on a cell, find which
         * cell it is in terms of (row, col) & then
         * send the move to the server
         */
        DOM.board.$board.find("td").click(function(e) {
            var $select_row = $(this).parent();
            var $select_cell = $(this);
    
            var row = 1;
            DOM.board.$board.find("tr").each(function() {
                if($select_row[0] == $(this)[0]) {
                    return false;
                }
                row++;
            });
    
            var cell = 1;
            $select_row.find("td").each(function() {
                if($select_cell[0] == $(this)[0]) {
                    return false;
                }
                cell++;
            });

        
            /*
                Send a request to the application server
            */
           //sendRequest("", function() { return true; });
           sendRequest(
                {
                    action: "move",
                    row: row,
                    col: cell
                },

                /*
                    This is the response from
                    the application server
                */
                function(data) {
                    console.log("DEBUG: incoming data from application server: " + data);

                    if(Object.keys(data).length < 1) {
                        //Flash the cell red, indicating bad move or error
                        $select_cell.animate({backgroundColor:"red"});
                        $select_cell.animate({backgroundColor: "#808080"});
                        return false;
                    }

                    setCellValue(row, cell, "X");

                    //Is there a declared winner?
                    if(data.winner) {
                        if(data.winner == "Player") {
                            DOM.board.$winner.text("Player wins").effect("highlight");
                            return true;
                        }
                        else if(data.winner == "Computer") {
                            DOM.board.$winner.text("Computer wins").effect("highlight");
                        }
                        else if(data.winner == "Tie") {
                            DOM.board.$winner.text("It's a tie!").effect("highlight");
                        }
                    }

                    setCellValue(data.move[0], data.move[1], "O");
                    return true;
                }
            );

        });

        /*
            Resetting the game
        */
       DOM.board.$reset.click(function(e) {
            sendRequest(
                {
                    action: "reset"
                },
                //Wait for a response before resetting the client's board
                function(data) {
                    DOM.board.$board.find("td").text("");
                    DOM.board.$winner.text("");
                }
            )
            
        });
    }
    return {
        init: init()
    };
}());