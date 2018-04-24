import random
import json

class Board:
    def __init__(self):
        self.board = []
        self.winner = None
        self.available = []
        self.reset()
        

    def reset(self):
        self.board = [
            [None for x in range(0,3)]
            for y in range(0,3)
        ]

        self.winner = None
        self.available = self.available_moves()

    def get_board(self):
        return self.board


    def player_move(self, move : (int,int)) -> str:
        print(self.winner)
        if self.winner != None:
            return "{}"

        (row, col) = move
        pcmove = (3,3)

        #Assure position isn't taken!
        if (row, col) not in self.available:
            return "{}"

        self.board[row][col] = "Player"
        self.available.remove((row,col))

        #Has the player won?
        if self.game_over((row,col)):
            response = json.JSONEncoder().encode({"winner":self.winner, "move":(3,3)})
            #self.reset()
            return response

        pcmove = self.pc_move()
        pcrow = pcmove[0]
        pccol = pcmove[1]

        if self.game_over((pcrow, pccol)):
            response = json.JSONEncoder().encode({"winner":self.winner, "move":(pcrow+1, pccol+1)})
            #self.reset()
            return response

        response = json.JSONEncoder().encode({"winner":self.winner, "move":(pcrow+1, pccol+1)})
        
        return response
        



    def pc_move(self) -> (int,int):
        move = random.choice(self.available)
        print(self.available)
        row = move[0]
        col = move[1]
        self.board[row][col] = "Computer"
        self.available.remove(move)
        return (row,col)




    def available_moves(self) -> []:
        pairs = []

        for row in range(0, len(self.board)):
            for col in range(0, len(self.board)):

                if self.board[row][col] == None:
                    pairs.append((row,col))

        return pairs


    
    def game_over(self, move : (int,int)) -> bool:
        (row, col) = move

        if self.check_column(col) or self.check_vertical(row) or self.check_diag_left() or self.check_diag_right():
            self.winner = self.board[row][col]
        elif len(self.available) < 1:
            self.winner = "Tie"
        else:
            return False

        return True


    def check_column(self, col : int) -> bool:
        for row in range(1, len(self.board)):
            if self.board[row - 1][col] != self.board[row][col]:
                return False
        return True

    def check_vertical(self, row : int) -> bool:
        for col in range(1, len(self.board)):
            if self.board[row][col] == None:
                return False

            if self.board[row][col - 1] != self.board[row][col]:
                return False
        return True

    def check_diag_left(self) -> bool:
        for cell in range(1, len(self.board)):
            if self.board[cell][cell] == None:
                return False

            if self.board[cell - 1][cell - 1] != self.board[cell][cell]:
                return False
        return True

    def check_diag_right(self) -> bool:
        if self.board[0][2] == self.board[1][1] and self.board[1][1] == self.board[2][0] and self.board[1][1] != None:
                return True
        else:
            return False