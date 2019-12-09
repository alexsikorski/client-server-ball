package GameClient;

import Utilities.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Client extends Thread {
    private static Scanner scanner;
    private static Socket socket;
    private static DataInputStream dis;
    private static DataOutputStream dos;

    public static void main(String[] args) {
        Thread thread = new Client();
        thread.start();
    }

    @Override
    public void run() {
        try {
            scanner = new Scanner(System.in); // gets input
            socket = new Socket(Constants.host, Constants.port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            //System.out.println(Thread.activeCount());
            Boolean hasBall;
            try { // try data input stream
                while (dis.available() > 0) {
                    System.out.println(dis.readUTF());
                    hasBall = dis.readBoolean();
                    if (hasBall){ // if client has ball (send boolean, listen for boolean)
                        System.out.println("YOU HAVE THE BALL!!! Who would you like to pass it to? If you would like to refresh player list, enter 0.");
                        int toSend = Integer.parseInt(scanner.nextLine());
                        dos.writeInt(toSend); // send the integer
                    }
                    else dos.writeInt(0); // input
                }

            } catch (IOException e) {
                e.printStackTrace();
                // close
                scanner.close();
                try {
                    dis.close();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

}

