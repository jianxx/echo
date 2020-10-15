# echo
This is a sample project to use grpc with golang/java and bazel.
And this project is developed with vscode.

echo_server is implemented with Golang and echo_client is implemented with Java.

v0.1: A simple echo program with socket connection.
v0.2: Use protobuf as message structure.
v0.3: Use grpc as communication protocol.

There are several things I learned in development phase:
1. Use maven to manage java project is still a good idea even when I'm using bazel.
2. rules_proto_grpc is better than the native proto support of bazel.
