import server
import re


def on_incoming_data(data : str) -> str:
    move = int()

    try:
        move = get_player_move(data)
    except ValueError:
        return ""
    return str(move)

def get_player_move(data : str) -> int:
    result = re.search("moveInput=(\d){1}", data[0:12])
    try:
        return int(result.group(1))
    except:
        raise ValueError("Regex fault")
    return None

server = server.Server("127.0.0.1", 5050, on_incoming_data)
server.start()