package echo

import (
	"context"
	"echo"
	"log"
	"net"
	"testing"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"google.golang.org/grpc/test/bufconn"
)

func dialer() func(context.Context, string) (net.Conn, error) {
	listener := bufconn.Listen(1234)

	server := grpc.NewServer()

	echo.RegisterTransceiverServer(server, &EchoServer{})

	go func() {
		if err := server.Serve(listener); err != nil {
			log.Fatal(err)
		}
	}()

	return func(context.Context, string) (net.Conn, error) {
		return listener.Dial()
	}
}

func TestEchoServer_Echo(t *testing.T) {
	tests := []struct {
		inputMessage string
		res          *echo.EchoResponse
		errCode      codes.Code
		errMsg       string
	}{}

	ctx := context.Background()

	conn, err := grpc.DialContext(ctx, "", grpc.WithInsecure(), grpc.WithContextDialer(dialer()))
	if err != nil {
		log.Fatal(err)
	}
	defer conn.Close()

	client := echo.NewTransceiverClient(conn)

	for _, tt := range tests {
		t.Run(tt.inputMessage, func(t *testing.T) {
			fromClient := echo.TransmissionObject{
				Message: tt.inputMessage,
				Value:   0.01,
			}
			request := &echo.EchoRequest{FromClient: &fromClient}

			response, err := client.Echo(ctx, request)

			if response != nil {
				if response.GetFromServer() != tt.res.GetFromServer() {
					t.Error("response: expected", tt.res.GetFromServer(), "received", response.GetFromServer())
				}
			}

			if err != nil {
				if er, ok := status.FromError(err); ok {
					if er.Code() != tt.errCode {
						t.Error("error code: expected", codes.InvalidArgument, "received", er.Code())
					}
					if er.Message() != tt.errMsg {
						t.Error("error message: expected", tt.errMsg, "received", er.Message())
					}
				}
			}
		})
	}
}
