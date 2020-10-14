package com.github.jianxx.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

import echo.TransmissionObjectOuterClass.TransmissionObject;

/**
 * EchoClient
 * 
 * @author: Xiaoxiang JIAN
 */
public class EchoClient {
    private static Logger logger = Logger.getLogger(EchoClient.class.getCanonicalName());

    private Socket socketToServer = null;

    public EchoClient() throws IOException {
        this(new Socket("localhost", 1234));
    }

    public EchoClient(Socket s) {
        this.socketToServer = s;
    }

    public void start() throws IOException {
        try (final BufferedReader inputFromServer = new BufferedReader(
                new InputStreamReader(socketToServer.getInputStream()));
                final BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));) {
            logger.info("Waiting on input from the user...");
            final String inputFromUser = commandLineInput.readLine();
            if (inputFromUser != null) {
                logger.info(String.format("Received by Java: %s", inputFromUser));
                TransmissionObject transmissionObject = TransmissionObject.newBuilder().setMessage(inputFromUser)
                        .setValue(3.145f).build();
                transmissionObject.writeTo(socketToServer.getOutputStream());

                TransmissionObject receivedObject = TransmissionObject.parseFrom(socketToServer.getInputStream());
            }
        } finally {

            socketToServer.close();
        }

    }

    public static void main(String args[]) {
        logger.info("Spinning up the Echo Client in Java...");
        try {
            EchoClient client = new EchoClient();
            client.start();
        } catch (Exception e) {
            logger.severe("Error: " + e.getMessage());
        }
    }
}