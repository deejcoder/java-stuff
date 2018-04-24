def game_over(move) -> int:
    global g_winner
    (row, col) = move

    if available_moves() == []:
        g_winner = "Tie"
    if check_column(col) or check_vertical(row) or check_diag_left() or check_diag_right():
        g_winner = g_board[row][col]
    else:
        return False

    return True

def check_column(col : int) -> bool:
    for row in range(1, len(g_board)):
        if g_board[row - 1][col] != g_board[row][col]:
            return False
    return True

def check_vertical(row : int) -> bool:
    for col in range(1, len(g_board)):
        if g_board[row][col] == None:
            return False

        if g_board[row][col - 1] != g_board[row][col]:
            return False
    return True

def check_diag_left() -> bool:
    for cell in range(1, len(g_board)):
        if g_board[cell][cell] == None:
            return False

        if g_board[cell - 1][cell - 1] != g_board[cell][cell]:
            return False
    return True

def check_diag_right() -> bool:
    if g_board[0][2] == g_board[1][1] and g_board[1][1] == g_board[2][0] and g_board[1][1] != None:
        return True
    else:
        return False