function setCellValue(row, col, value) {
    gridRow = $("#gameboard tr:nth-child(" + row + ")");
    gridRow.find("td:nth-child(" + col + ")").text(value);

    console.log("DEBUG: set cell (" + row + "," + col + ") to " + value);
    return true;
}