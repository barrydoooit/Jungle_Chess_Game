package hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Random;

/**
 * this class is used for server connection
 */
public class Server extends User {
    private ServerSocket serverSocket;

    /**
     * @param port the port number for establishing a connection
     */
    public Server(final int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Your IP is: " + InetAddress.getLocalHost().getHostAddress().trim() + "\nPort number is: " + getLocalPort());
            System.out.println("Waiting for player to join...");
            socket = serverSocket.accept();
            System.out.println("Player Joined!");
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            setFaction();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void setFaction() throws IOException {
        Faction faction = new Random().nextInt(2) + 1 == 1 ? Faction.X : Faction.Y;
        this.faction = faction;
        sendMessage(faction.toString());
    }

    @Override
    public void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Failed to terminate port listener!");
        }
    }

    @Override
    public int getLocalPort(){
        return serverSocket.getLocalPort();
    }
}
