import socket
import queue

class Server():
    def __init__(ip : str, port : int, on_data_func : callable):

        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.setblocking(False)
        self._on_data_func = on_data_func
        
        #Queues are thread safe in Python
        #sockets to accept/read from
        self.inputs = [server]
        #sockets to write to in form socket:[messages]
        self.outputs = queue.Queue()

        self.addr = (ip, port)
        self.buffer_size = 516

    def start(self):
        self.server.bind(self.addr)
        self.server.listen()

        while inputs:
            readable, writable, exception = select.select(inputs, outputs, inputs)

            for sock in readable:
                self.on_incoming_data(sock)

            for sock in writable:
                self.on_write_data(sock)

            for sock in exception:
                self.on_socket_error(sock)


    def on_incoming_data(self, sock : socket):
        if sock is server:
            #accept a client
        else:
            #the client has sent data
            data = sock.recv(self.buffer_size).decode("utf-8")
            if data:
                self._on_data_func(data)
            else:
                if sock in outputs:
                    outputs.remove(sock)
                inputs.remove(sock)
                sock.close()
