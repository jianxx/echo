
#include <grpcpp/grpcpp.h>

#include <iostream>
#include <memory>
#include <string>

#include "proto/transceiver.grpc.pb.h"

using echo::EchoRequest;
using echo::EchoResponse;
using echo::Transceiver;
using echo::TransmissionObject;
using grpc::Channel;
using grpc::ClientContext;
using grpc::Status;

class EchoClient {
   public:
    EchoClient(std::shared_ptr<Channel> channel) : stub(Transceiver::NewStub(channel)) {}

    std::string Echo(const std::string& inputMessage) {
        TransmissionObject* object = new TransmissionObject();
        object->set_message(inputMessage);
        EchoRequest request;
        request.set_allocated_from_client(object);

        EchoResponse response;

        ClientContext context;

        std::cout << "Before grpc invocation" << std::endl;
        Status status = stub->Echo(&context, request, &response);
        std::cout << "After grpc invocation" << std::endl;

        if (status.ok()) {
            std::cout << "rpc successful" << std::endl;
            return response.from_server().message();
            // return response.from_server().message();
        } else {
            return "RPC failed";
        }
    }

   private:
    std::unique_ptr<Transceiver::Stub> stub;
};

int main(int argc, char** argv) {
    // Instantiate the client. It requires a channel, out of which the actual RPCs
    // are created. This channel models a connection to an endpoint specified by
    // the argument "--target=" which is the only expected argument.
    // We indicate that the channel isn't authenticated (use of
    // InsecureChannelCredentials()).
    std::string target_str;
    std::string arg_str("--target");
    if (argc > 1) {
        std::string arg_val = argv[1];
        size_t start_pos = arg_val.find(arg_str);
        if (start_pos != std::string::npos) {
            start_pos += arg_str.size();
            if (arg_val[start_pos] == '=') {
                target_str = arg_val.substr(start_pos + 1);
            } else {
                std::cout << "The only correct argument syntax is --target=" << std::endl;
                return 0;
            }
        } else {
            std::cout << "The only acceptable argument is --target=" << std::endl;
            return 0;
        }
    } else {
        target_str = "localhost:1234";
    }

    EchoClient client(grpc::CreateChannel(
        target_str, grpc::InsecureChannelCredentials()));
    std::string message("hello");
    std::string response = client.Echo(message);
    std::cout << "Message received: " << response << std::endl;

    return 0;
}