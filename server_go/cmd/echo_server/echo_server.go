package main

import (
	"context"
	"echo"
	"fmt"
	"log"
	"net"

	"google.golang.org/grpc"
)

// EchoServer ...
type EchoServer struct{}

// Echo ...
func (es *EchoServer) Echo(context context.Context, request *echo.EchoRequest) (*echo.EchoResponse, error) {
	log.Println("Message = " + (*request).FromClient.GetMessage())
	log.Println(fmt.Sprintf("Value = %f", (*request).FromClient.GetValue()))
	serverMessage := "Received from client: " + (*request).FromClient.GetMessage()
	serverValue := (*request).FromClient.Value * 2
	fromServer := echo.TransmissionObject{
		Message: serverMessage,
		Value:   serverValue,
	}
	return &echo.EchoResponse{
		FromServer: &fromServer,
	}, nil
}

func main() {
	log.Println("Spinning up the Echo Server in Go...")
	listen, error := net.Listen("tcp", ":1234")
	if error != nil {
		log.Panicln("Unable to listen: " + error.Error())
	}
	defer listen.Close()
	grpcServer := grpc.NewServer()
	echo.RegisterTransceiverServer(grpcServer, &EchoServer{})
	error = grpcServer.Serve(listen)
	if error != nil {
		log.Panicln("Unable to start serving! Error: " + error.Error())
	}
}
