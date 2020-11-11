package main

import (
	"echo"
	"echo_server"
	"log"
	"net"

	"google.golang.org/grpc"
)

func main() {
	log.Println("Spinning up the Echo Server in Go...")
	listen, error := net.Listen("tcp", ":1234")
	if error != nil {
		log.Panicln("Unable to listen: " + error.Error())
	}
	defer listen.Close()
	grpcServer := grpc.NewServer()
	echo.RegisterTransceiverServer(grpcServer, &echo_server.EchoServer{})
	error = grpcServer.Serve(listen)
	if error != nil {
		log.Panicln("Unable to start serving! Error: " + error.Error())
	}
}
