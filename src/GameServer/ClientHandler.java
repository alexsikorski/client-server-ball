package GameServer;

import Utilities.Constants;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutput dos;
    final Socket socket;
    public int clientID;
    public Timer timer = new Timer();
    Ball ball = new Ball();


    public ClientHandler(DataInputStream dis, DataOutput dos, Socket socket, int clientID) {
        this.dis = dis;
        this.dos = dos;
        this.socket = socket;
        this.clientID = clientID;
    }

    public int returnClientID(){
        int cID = this.clientID;
        return cID;
    }

    public void disconnectPlayer(TimerTask MyTimerTask) throws IOException {
        System.out.println("Player: " + this.clientID + ", " + " Connection closed.");
        Server.connectedClients.remove(this.socket, this.clientID); // removes player

        // ball
        ball.removeBall(this.clientID);
        Server.gameState.remove(this.clientID); // removes player from state
        ball.disconnectBall();

        System.out.println();
        System.out.println("CONNECTED PLAYERS: ");
        if (Server.stringPlayers().isEmpty()) {
            System.out.println("No players.");
            System.out.println();
        } else {
            System.out.println(Server.stringPlayers());
        }

        this.socket.close();
        timer.cancel();
        timer.purge();
        MyTimerTask.cancel();
    }

    @Override
    public void run() {
        timer.schedule(new MyTimerTask(), 0, Constants.delay);

    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            int received;
            // System.out.println("        Active threads: "+ Thread.activeCount());
            try {
                try {
                    ClientHandler.this.dos.writeUTF("You are Player: " + returnClientID() + "\n" + "CONNECTED PLAYERS: \n" + Server.stringPlayers() + "\n" + "Player: " + ball.whoHasBall() + " has the ball!\n");
                    ClientHandler.this.dos.writeBoolean(ball.doesClientHaveBall(ClientHandler.this.clientID));
                    received = dis.readInt();
                    if (received > 0){
                        // pass ball to received
                        ball.giveBall(received, ClientHandler.this.clientID);
                    }

                } catch (SocketException e) {
                    disconnectPlayer(MyTimerTask.this); // because timer is implemented, need to cancel sockets and timers
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}



