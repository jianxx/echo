import grpc
from concurrent import futures
import time
import proto.transceiver_pb2_grpc as pb2_grpc
import proto.transceiver_pb2 as pb2
import proto.transmission_object_pb2 as to_pb2


class EchoClient(object):
    """
    Client for gRPC functionality
    """

    def __init__(self):
        self.host = "localhost"
        self.server_port = 1234

        # instantiate a channel
        self.channel = grpc.insecure_channel(
            "{}:{}".format(self.host, self.server_port)
        )

        # bind the client and the server
        self.stub = pb2_grpc.TransceiverStub(self.channel)

    def echo(self, message):
        """
        Client function to call the rpc for GetServerResponse
        """
        message = pb2.EchoRequest(from_client=message)
        print(f"{message}")
        return self.stub.Echo(message)


if __name__ == "__main__":
    client = EchoClient()

    result = client.echo(to_pb2.TransmissionObject(value=123, message="Hello"))
    print(f"{result}")

