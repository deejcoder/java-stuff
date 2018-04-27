import socket
import inspect

"""
    If it were me, I would've implemented the Java web server
    using non-blocking sockets (asynchronous) and likewise with this server
    for handling multiple clients at once

    For now I have tried making it as scalable as possible
"""
class Server:
    #Initialize the server, but do not start it.
    def __init__(self, host : str, port : int, on_data_func : callable):

        #validate the provided callback
        self.is_valid_data_callback(on_data_func)

        self.host = host
        self.port = port
        self.BUFFER_SIZE = 516
        self.on_incoming_data = on_data_func

        #Create a TCP socket
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.shutdown = True
        
    
    def start(self):
        #Bind the socket to an address
        self.socket.bind((self.host, self.port))
        #Start listening on the socket
        self.socket.listen()
        self.shutdown = False

        #Recieve data until the server has been shutdown
        while not self.shutdown:
            (sock, addr) = self.socket.accept()
            with sock:
                try:
                    data = sock.recv(self.BUFFER_SIZE).decode("utf-8")
                except UnicodeDecodeError:
                    sock.send("Invalid Unicode detected.".encode())

                if(data):
                    #Forward the incoming data to a callback
                    response = self.on_incoming_data(data)

                    #The callback will return a String, send this string
                    sock.send(response.encode())
                else:
                    sock.send("There was no data passed.".encode())

                sock.close()

    def close(self):
        self.shutdown = True

    """
    Validating the callback: assure it has the suitable static types defined,
    and suitable number of parameters.
    """
    def is_valid_data_callback(self, func : callable):
        params = inspect.signature(func).parameters
        return_type = inspect.signature(func).return_annotation
        if str(params['data']) != "data:str":
            raise NameError("ServerError: function {0} must have a 'data:str' parameter".format(func))

        if len(params) != 1:
            raise NameError("ServerError: function {0} must have only one parameter".format(func))
        
        if str(return_type) == "str":
            raise NameError("ServerError: function {0} must have a str return type specified".format(func))
            
        return True







