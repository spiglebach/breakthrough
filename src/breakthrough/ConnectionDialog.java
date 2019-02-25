/*
 * Készítette:
 * Név: Szukács Marcell
 * Neptun-kód: ORDE4C
 * E-mail: spiglebach@gmail.com
 */
package breakthrough;

import java.awt.Component;
import java.awt.Container;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.WindowConstants;

/**
 *
 * @author spigl
 */
public class ConnectionDialog  extends JDialog{
    JLabel ipLabel;
    JTextField serverIP;
    JButton btn;
    GameType gt;
    Socket client=null;
    ServerSocket server=null;
    Container cp;
    InetAddress ip;
    MainWindow mw;
    ConnectionDialog(MainWindow mw, GameType gt) throws IOException{
        super(mw, true);
        this.mw=mw;
        this.gt=gt;
        cp=getContentPane();
        if(gt==GameType.HOST){
            setTitle("Szerver indítása");
            server=new ServerSocket(22220);
            //server.setSoTimeout(15000);
        }else{
            setTitle("Csatlakozás szerverhez");
        }
        //setSize(400,400);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        //addWindowListener(closeOperation);
        ipLabel=new JLabel("A szerver IP címe:");
        ipLabel.setFont(MainWindow.MAIN_FONT);
        serverIP=new JTextField(15);
        serverIP.setFont(MainWindow.MAIN_FONT);
        btn=new JButton();
        btn.setFont(MainWindow.MAIN_FONT);
        if(GameType.HOST==gt){
            serverIP.setText(InetAddress.getLocalHost().getHostAddress());
            serverIP.setEditable(false);
            btn.setText("Játékos keresése");
            btn.addActionListener(waitForPlayer);
        }else{
            btn.setText("Csatlakozás");
            btn.addActionListener(connectToHost);
        }
        SpringLayout layout=new SpringLayout();
        cp.setLayout(layout);
        cp.add(ipLabel);
        cp.add(serverIP);
        
        cp.add(btn);
        
        
        layout.putConstraint(SpringLayout.WEST, ipLabel,5,SpringLayout.WEST, cp);
        layout.putConstraint(SpringLayout.NORTH, ipLabel,5,SpringLayout.NORTH, cp);
        layout.putConstraint(SpringLayout.WEST, serverIP,5,SpringLayout.EAST, ipLabel);
        layout.putConstraint(SpringLayout.NORTH, serverIP,5,SpringLayout.NORTH, cp);
        layout.putConstraint(SpringLayout.NORTH, btn, 5, SpringLayout.SOUTH, ipLabel);
        layout.putConstraint(SpringLayout.EAST, cp,5, SpringLayout.EAST, serverIP);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btn, 5, SpringLayout.HORIZONTAL_CENTER, cp);
        layout.putConstraint(SpringLayout.SOUTH, cp,5,SpringLayout.SOUTH, btn);
        
        
        setLocationRelativeTo(mw);
        pack();
        setVisible(true);
    }
    ActionListener connectToHost=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            try{
                String ipStr=serverIP.getText();
                ip=InetAddress.getByAddress(validateIP(ipStr));
                client=new Socket(ip, 22220);
                mw.setConnection(new Connection(client),gt);
                ConnectionDialog.this.setVisible(false);
            }catch(NumberFormatException e){
                System.out.println("Numformat");
            }catch(UnknownHostException e){
                System.out.println("Unknownhost");
            }catch(IOException e){
                System.out.println("IOEXC");
            }
        }
    };
    
    ActionListener waitForPlayer=new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent ae) {
            try {
                client=server.accept();
                if(server != null && client != null){
                    mw.setConnection(new Connection(server, client),gt);
                    ConnectionDialog.this.dispose();
                }
            }  catch (SocketTimeoutException e){
                client=null;
                ConnectionDialog.this.dispose();
            }catch (IOException ex) {
                Logger.getLogger(ConnectionDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
    byte[] validateIP(String ip) throws NumberFormatException, IOException{
        String[] components=ip.split("\\.");
        System.out.println(ip);
        if(components.length != 4){
            throw new IOException();
        }
        byte bytes[]=new byte[4];
        for(int i=0;i<4;++i){
                int num=Integer.parseInt(components[i]);
                bytes[i]=(byte) num;
        }
        return bytes;
    }
    private WindowListener closeOperation = new WindowAdapter(){
        @Override
        public void windowClosing(WindowEvent e) {
            try {
                if(server != null){
                    server.close();
                    server=null;
                }
                if(client !=null){
                    client.close();
                    client=null;
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            ConnectionDialog.this.dispose();
        }
    };
}
