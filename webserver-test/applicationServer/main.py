import server
import re
import json
import random

import board

board = board.Board()

def on_incoming_data(data : str) -> str:
    global board
    print(data)
    try:
        action = get_player_action(data)
    except ValueError:
        return "{}"

    if action == "reset":
        board.reset()
        return "{}"
    elif action == "getstate":
        array = board.get_board()
        return json.JSONEncoder().encode({"moves":array})
        
    elif action == "move":

        print("test")
        try:
            move = get_player_move(data)
            response = board.player_move(move)
            return response
        except ValueError:
            return "{}"
    else:
        return "{}"


def get_player_move(data : str) -> (int,int):
    result = json.loads(data)
    try:
        return (result['row'] - 1, result['col'] - 1)
    except:
        raise ValueError("Invalid JSON passed from server")
    return None

def get_player_action(data : str) -> str:
    result = json.loads(data)
    try:
        return result['action']
    except:
        raise ValueError("Invalid JSON passed from server")
    return None


server = server.Server("127.0.0.1", 5050, on_incoming_data)
server.start()
