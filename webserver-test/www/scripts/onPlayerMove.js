
$(document).ready(function() {
    var playerName = "Player";

    $("#gameboard tr td").click(function(e) {
        var $cur_row = $(this).parent();
        var $cell = $(this);

        var row = 0;

        $("#gameboard tr").each(function() {
            if($cur_row[0] == $(this)[0]) {
                return false;
            }
            row++;
        });

        var col = 0;
        $cur_row.find("td").each(function() {
            if($cell[0] == $(this)[0]) {
                return false;
            }
            col++;
        });

        if($cell.text().length > 1) {
            $cell.text("");
        }
        else {
            $cell.text("X");
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if(this.readyState == 4 && this.status == 200) {
                var jsonObj = JSON.parse(this.responseText);
                console.log(jsonObj);
                if(jsonObj.winner) {
                    if(jsonObj.winner == "Player") {
                        $(".winnerText").text("Player wins");
                        return true;
                    }
                    else if(jsonObj.winner == "Computer") {
                        $(".winnerText").text("Computer wins");
                    }
                }
                $("#gameboard tr:nth-child(" + jsonObj.move[0] + ")").find("td:nth-child(" + jsonObj.move[1] + ")").text("O");
                console.log("Computer selects row " + jsonObj.move[0] + " and col " + jsonObj.move[1]);
                return false;
            }
        };

        xhttp.open("POST", "/api/games/tictactoe.html", true);
        xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhttp.send(JSON.stringify({
            action: "move",
            row: row,
            col: col}
        ));

    })
});