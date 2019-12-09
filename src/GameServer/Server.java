package GameServer;

import Utilities.Constants;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.TreeMap;

public class Server {
    public static HashMap<Socket, Integer> connectedClients = new HashMap<>();
    public static TreeMap<Integer, Boolean> gameState = new TreeMap<>();     // Integer clientID, Boolean hasBall
    private static int clientID = 0;
    private static DataOutputStream dos = null;

    public static String stringPlayers() {
        String string = "";
        for (int value : Server.connectedClients.values()) {
            string = string + "Client ID: " + value + "\n";
        }
        return string;
    }

    public static void checkIfFirstPlayer(int clientID) {
        if (Server.gameState.isEmpty()) { // if no players
            Server.gameState.put(clientID, true); // first player gets ball
            System.out.println("First player! Ball is handed to ClientID: " + clientID);
        } else {
            Server.gameState.put(clientID, false); // else we just put in other players as false
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome. Waiting for connections...");
        // Socket listening
        ServerSocket serverSocket = new ServerSocket(Constants.port, 0, Constants.host);
        // Loop for getting client request
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                // clientID increment
                clientID++;
                connectedClients.put(socket, clientID); // adds to hashmap
                checkIfFirstPlayer(clientID); // checks if first player then adds to treemap

                System.out.println("ClientID: " + clientID + ", " + " Connected successfully.");
                System.out.println();
                System.out.println("CONNECTED PLAYERS: ");
                System.out.println(stringPlayers());
                //connectedClients.forEach((key, value) -> System.out.println(key + ":" + value));

                // obtain input/output streams
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutput dos = new DataOutputStream(socket.getOutputStream());
                Server.dos = (DataOutputStream) dos;

                // creating new thread for object
                Thread thread = new ClientHandler(dis, dos, socket, clientID);
                // starting thread
                thread.start();
            } catch (Exception e) {
                socket.close(); // closes socket
                e.printStackTrace();
            }
        }
    }
}


