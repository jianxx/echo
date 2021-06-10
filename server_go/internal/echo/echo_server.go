package echo

import (
	"context"
	"echo"
	"fmt"
	"log"
)

// EchoServer ...
type EchoServer struct {
	echo.UnimplementedTransceiverServer
}

// Echo ...
func (es *EchoServer) Echo(context context.Context, request *echo.EchoRequest) (*echo.EchoResponse, error) {
	log.Println("Message = " + (*request).FromClient.GetMessage())
	log.Println(fmt.Sprintf("Value = %f", (*request).FromClient.GetValue()))
	serverMessage := (*request).FromClient.GetMessage()
	serverValue := (*request).FromClient.Value * 2
	fromServer := echo.TransmissionObject{
		Message: serverMessage,
		Value:   serverValue,
	}
	return &echo.EchoResponse{
		FromServer: &fromServer,
	}, nil
}
