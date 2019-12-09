package GameServer;

import java.util.Map;

public class Ball {


    public void giveBall(int received, int currentID) {
        // validation
        if (doesClientExist(received) && received != currentID){ // if client exists and isnt him/herself
            System.out.println("Player: " + currentID + " passes ball to Player: " + received + ".");
            Server.gameState.replace(received, true); // replaces clientID's value with true
            removeBall(currentID);
        }
        else if (received == currentID){ // if player == player
            System.out.println("Player: " + received + " is throwing the ball back to him/herself...");
        }
        else{
            System.out.println("Player: " + received + " does not exist, returning ball!");
            Server.gameState.replace(currentID,true);
        }
    }

    public void removeBall(int clientID) {
        Server.gameState.replace(clientID, false); // false for no ball
    }

    public int whoHasBall() {
        int clientID = 0;
        for (Map.Entry entry : Server.gameState.entrySet()) { // for loop in gameState
            if ((boolean) entry.getValue()) { // if entry value is true (has ball)
                clientID = (int) entry.getKey(); // set clientID to the key of the entry which value is true
            }
        }
        return clientID;
    }
    public boolean doesClientExist(int clientID) {
        boolean isEquiv = false;
        for (Integer entryID : Server.gameState.keySet()) { // for loop in gamestate keys
            if (entryID == clientID) { // if existing key exists
                isEquiv = true; // return true
                break;
            }
        }
        return isEquiv;
    }


    public boolean doesClientHaveBall(int clientID){ // this is the client we are comparing to
        int clientIDHasBall = whoHasBall(); // this client has the ball
        if (clientIDHasBall == clientID){ // if this client has ball
            return true;
        }
        else{
            return false;
        }

    }

    public void disconnectBall() { // when player with ball leaves, give a player the ball
        if (Server.gameState.containsValue(false) && Server.gameState.size() >= 1) { // if no one has ball and more than 1 player
            Map.Entry<Integer, Boolean> lastEntry = Server.gameState.lastEntry(); // finds last entry
            System.out.println("Automatically passing ball to ClientID: " + lastEntry.getKey());
            Server.gameState.replace(lastEntry.getKey(), true); // uses last entry key and replaces its bool value with true

        }
        if (Server.gameState.isEmpty()) {
            System.out.println("No one to give the ball to!");
        }
    }
}

