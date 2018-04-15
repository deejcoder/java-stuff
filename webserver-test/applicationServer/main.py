import server
import re
import json

#generate a 3x3 gameboard
g_board = [[None for x in range(0, 3)] for y in range(0,3)]
g_winner = None
counter = 1


def on_incoming_data(data : str) -> str:

    try:
        (row, col) = get_player_move(data)
    except ValueError:
        return ""


    global g_board
    g_board[row][col] = 1 # let 1 represent player
    pc_move = make_move()
    response = json.JSONEncoder().encode({"winner":g_winner, "move":pc_move})
    return response

def make_move() -> (int, int):
    global g_board
    global g_winner
    global counter
    move = (counter,counter)
    counter += 1
    if counter > 3:
        counter = 1
    return move



def get_player_move(data : str) -> (int,int):
    result = json.loads(data)
    try:
        return (result['row'], result['col'])
    except:
        raise ValueError("Invalid JSON passed from server")
    return None

server = server.Server("127.0.0.1", 5051, on_incoming_data)
server.start()