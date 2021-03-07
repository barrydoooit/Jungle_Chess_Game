package hk.edu.polyu.comp.comp2021.jungle.ConnectionAndStorage;

import hk.edu.polyu.comp.comp2021.jungle.model.GameBoard.Faction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * and abstract class for setting connection and transferring data
 */
public abstract class User {
    /**
     * for data input
     */
    protected DataInputStream dataInputStream;
    /**
     * for data output
     */
    protected DataOutputStream dataOutputStream;
    /**
     * the socket use for connection
     */
    protected Socket socket;
    /**
     * the faction of player
     */
    protected Faction faction;

    /**
     * name of player
     */
    protected String playerName;
    /**
     * Set player name
     * @param playerName entered player's name
     */
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
    /**
     * Get player name
     * @return player's name
     */
    public String getPlayerName(){
        return this.playerName;
    }
    /**
     * send a message to other user
     * @param str message
     */
    public void sendMessage(final String str){
        try{
            dataOutputStream.writeUTF(str);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * @return the message read from paired server/client
     * @throws IOException when read from inputStream failed
     */
    public String readMessage()throws IOException{
        return dataInputStream.readUTF();
    }

    /**
     * @return the player faction
     */
    public Faction getFaction(){return faction;}

    /**
     * set player faction
     * @throws IOException when failed to receive or send info of faction
     */
    protected abstract void setFaction() throws IOException;

    /**
     * disconnect from socket network
     */
    public void disconnect(){
        try {
            socket.close();
        }catch(IOException ex){
            System.out.println(ex+ ": Fail to disconnect");
        }
    }

    /**
     * method to get the free port, only used for server
     * @return a free port
     */
    public int getLocalPort(){
        return 0;
    }
}
