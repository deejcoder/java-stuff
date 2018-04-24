
$(document).ready(function() {
    $("#button_reset").click(function(e) {
        var xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/tictactoe.html", true);
        xhttp.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhttp.send(JSON.stringify({ action: "reset" }));
        $("#gameboard td").text("");
    });
});