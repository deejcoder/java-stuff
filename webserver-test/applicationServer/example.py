import server

#define a callback
def on_incoming_data(data : str) -> str:
    # Print the incoming data
    print(data)

    #Send some data back to the client
    return "Some data..."

#Create a Server object, pass the defined callback
server = server.Server("127.0.0.1", 8080, on_incoming_data)
server.start()
