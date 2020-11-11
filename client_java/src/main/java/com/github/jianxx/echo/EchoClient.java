package com.github.jianxx.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import echo.TransceiverGrpc;
import echo.TransceiverOuterClass.EchoRequest;
import echo.TransceiverOuterClass.EchoResponse;
import echo.TransmissionObjectOuterClass.TransmissionObject;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * EchoClient
 * 
 * @author: Xiaoxiang JIAN
 */
public class EchoClient implements AutoCloseable {
    private static Logger logger = Logger.getLogger(EchoClient.class.getCanonicalName());

    private ManagedChannel channel = null;
    private TransceiverGrpc.TransceiverBlockingStub stub = null;

    public EchoClient() throws IOException {
        this("localhost", 1234);
    }

    public EchoClient(String hostname, int port) {
        this.channel = ManagedChannelBuilder.forAddress(hostname, port).usePlaintext().build();
        this.stub = TransceiverGrpc.newBlockingStub(this.channel);
    }

    public EchoClient(ManagedChannel channel) {
        this.channel = channel;
        this.stub = TransceiverGrpc.newBlockingStub(this.channel);
    }

    public String echo(String inputFromUser) {
        logger.info(String.format("Received by Java Client: %s", inputFromUser));
        EchoRequest request = EchoRequest.newBuilder()
                .setFromClient(TransmissionObject.newBuilder().setMessage(inputFromUser).setValue(3.145f).build())
                .build();
        EchoResponse response = stub.echo(request);
        logger.info(String.format("Received Message from server: %s", response.toString()));
        return response.toString();
    }

    public void close() throws IOException {
        channel.shutdownNow();
    }

    public static void main(String args[]) {
        logger.info("Spinning up the Echo Client in Java...");
        try (final EchoClient client = new EchoClient();
                final BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));) {
            logger.info("Waiting on input from the user...");
            final String inputFromUser = commandLineInput.readLine();
            client.echo(inputFromUser);
        } catch (Exception e) {
            logger.severe("Error: " + e.getMessage());
        }
    }
}