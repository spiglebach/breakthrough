/*
 * Készítette:
 * Név: Szukács Marcell
 * Neptun-kód: ORDE4C
 * E-mail: spiglebach@gmail.com
 */
package breakthrough;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author spigl
 */
public class Connection {
    ServerSocket server=null;
    Socket client = null;
    DataInputStream is = null;
    PrintStream os = null;
    final static int SERVER_TIMEOUT_IN_MILLIS = 10000;
    
    // clientside connection
    public Connection(Socket s) throws IOException{
        if(s != null){
            client=s;
            os=new PrintStream(client.getOutputStream());
            is=new DataInputStream(client.getInputStream());
        }
    }
    // serverside connection
    public Connection(ServerSocket ss, Socket s) throws IOException{
        if(s != null && ss != null)
            server=ss;
            server.setSoTimeout(SERVER_TIMEOUT_IN_MILLIS);
            client=s;
            is=new DataInputStream(client.getInputStream());
            os= new PrintStream(client.getOutputStream());
    }
    
    public boolean isServer(){
        return server != null;
    }
    
    public void close(){
        try {
            if(server != null) server.close();
            if(client != null) client.close();
        } catch (IOException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sendLine(String msg){
        os.println(msg);
    }
    public int processBoardSize() throws IOException{
        String s= is.readLine();
        int boardSize=Integer.parseInt(s);
        return boardSize;
    }
    public int[] processLine() throws IOException, NumberFormatException, ExitException{
            String s=is.readLine();
            String nums[]=s.split(",");
            if(nums.length != 4){
                if(nums.length == 1 && nums[0].equals("EXIT")){
                    throw new ExitException();
                }
                throw new NumberFormatException();
            }
            int fromrow=Integer.parseInt(nums[0]);
            int fromcolumn=Integer.parseInt(nums[1]);
            int torow=Integer.parseInt(nums[2]);
            int tocolumn=Integer.parseInt(nums[3]);
            return new int[]{fromrow,fromcolumn,torow,tocolumn};
    }
}
