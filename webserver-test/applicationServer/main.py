import server
import re
import json
import random

import board

board = board.Board()

"""
This callback is invoked when data is recieved.
"""
def on_incoming_data(data : str) -> str:
    global board

    try:
        action = get_player_action(data)
    except ValueError:
        return "{}"

    #Reset the game board
    if action == "reset":
        board.reset()
        return "{}"

    #Send the current board to the client
    elif action == "getstate":
        array = board.get_board()
        return json.JSONEncoder().encode({"moves":array})

    #Allow the player to move
    elif action == "move":

        try:
            move = get_player_move(data)
            return board.player_move(move)
        except ValueError:
            return "{}"
    else:
        return "{}"


"""
Validate & get the data from the JSON string
"""
# What type of request is it?
def get_player_action(data : str) -> str:
    try:
        result = json.loads(data)
        return result['action']
    except:
        raise ValueError("Invalid JSON passed from server")
    return None

# Where did the player move?
def get_player_move(data : str) -> (int,int):
    try:
        result = json.loads(data)
        return (result['row'] - 1, result['col'] - 1)
    except:
        raise ValueError("Invalid JSON (or not JSON) passed from server")
    return None


# Start the server
server = server.Server("127.0.0.1", 5050, on_incoming_data)
server.start()
