$(document).ready(function() {
    $("#gameboard tr td").click(function(e) {
        e.stopPropagation();
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
            //$cell.text((row+1) + "," + (col+1));
            $cell.text("X");
        }

        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function() {
            if(this.readyState == 4 && this.status == 200) {
                var jsonObj = JSON.parse(this.responseText);
                $("#gameboard tr:nth-child(" + jsonObj.move[0] + ")").find("td:nth-child(" + jsonObj.move[1] + ")").text("O");
                //jsonObj.move[0]
                //document.write(this.responseText);

            }
        };

        xhttp.open("POST", "/game.html", true);
        xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhttp.send(JSON.stringify({row: row, col: col}));


        //document.write(row, col);



    })
});