package main

import (
	"context"
	"echo"
	"fmt"
	"log"
	"net"

	"google.golang.org/grpc"
)

type EchoServer struct{}

func (es *EchoServer) Echo(context context.Context, request *echo.EchoRequest) (*echo.EchoResponse, error) {
	log.Println("Message = " + (*request).FromClient.GetMessage())
	log.Println(fmt.Sprintf("Value = %f", (*request).FromClient.GetValue()))
	server_message := "Received from client: " + (*request).FromClient.GetMessage()
	server_value := (*request).FromClient.Value * 2
	from_server := echo.TransmissionObject{
		Message: server_message,
		Value:   server_value,
	}
	return &echo.EchoResponse{
		FromServer: &from_server,
	}, nil
}

func main() {
	log.Println("Spinning up the Echo Server in Go...")
	listen, error := net.Listen("tcp", ":1234")
	if error != nil {
		log.Panicln("Unable to listen: " + error.Error())
	}
	defer listen.Close()
	connection, error := listen.Accept()
	if error != nil {
		log.Panicln("Cannot accept a connection! Error: " + error.Error())
	}
	log.Println("Receiving on a new connection")
	defer connection.Close()
	defer log.Println("Connection now closed.")
	grpc_server := grpc.NewServer()
	echo.RegisterTransceiverServer(grpc_server, &EchoServer{})
	error = grpc_server.Serve(listen)
	if error != nil {
		log.Panicln("Unable to start serving! Error: " + error.Error())
	}
}
