package hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
*this class is used for client connection
 */
public class Client extends User {
    /**
     *
     * @param host the server's ip
     * @param port the port number
     * @throws Exception failed to establish client connection
     */
    public Client(final String host, final int port)throws Exception{
        try {
            socket = new Socket(host, port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            setFaction();
        }catch(RuntimeException ex) {
            System.out.println("Client is not able to be established with given ip and port!");
            throw ex;
        } catch(IOException ex){
            System.out.println("Problems in data stream construction!");
            throw ex;
        }
    }

    @Override
    protected void setFaction() throws IOException {
        this.faction = readMessage().equals("X")?Faction.Y:Faction.X;
    }


}
