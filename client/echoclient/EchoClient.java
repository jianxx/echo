package client.echoclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class EchoClient {
    private static Logger logger = Logger.getLogger(EchoClient.class.getCanonicalName());

    public void start() throws IOException {
        Socket socketToServer = null;
        try {
            socketToServer = new Socket("localhost", 1234);
            final BufferedReader inputFromServer = new BufferedReader(
                    new InputStreamReader(socketToServer.getInputStream()));
            final BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));
            logger.info("Waiting on input from the user...");
            final String inputFromUser = commandLineInput.readLine();
            if (inputFromUser != null) {
                logger.info(String.format("Received by Java: %s", inputFromUser));
                final PrintWriter outputToServer = new PrintWriter(socketToServer.getOutputStream(), true);
                outputToServer.println(inputFromUser);
                logger.info(inputFromServer.readLine());
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