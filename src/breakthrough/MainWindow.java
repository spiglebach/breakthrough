/*
 * Készítette:
 * Név: Szukács Marcell
 * Neptun-kód: ORDE4C
 * E-mail: spiglebach@gmail.com
 */
package breakthrough;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

/**
 *
 * @author spigl
 */
public class MainWindow extends JFrame {
    private BufferedImage icon;
    // A program szöveges részei által használt betűtípus
    public static final Font MAIN_FONT=new Font(Font.SANS_SERIF,Font.PLAIN,30);
    // A program menüsorának betűtipusa
    private static final Font MENU_FONT=new Font(Font.SANS_SERIF,Font.PLAIN,20);
    // A főablak menüsora
    private final JMenuBar mainMenu;
    // A győztest kiíró szöveg(alapból új játék kezdésre szólít fel)
    private final JLabel mainLabel;
    // A főpanel, aminek a helyére kerül a játékpanel
    private final JPanel mainPanel;
    private Connection c=null;
    public MainWindow(){
        try {
            icon=ImageIO.read(new File("resources/icon.png"));
            setIconImage(icon);
        } catch (IOException ex) {
            System.out.println("Image not found!");
        }
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);    // X-el kilépés
        addWindowListener(exitOperation);
        setTitle("Multiplayer Játék");                   // Ablak címe
        //Fullscreen-re állítás
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(100, 100, (int) dim.getWidth()/3, (int) dim.getHeight()/2);
        setLocationRelativeTo(null);                // Ablak középre helyezése
        setResizable(false);
        
        mainMenu = new JMenuBar();
        // "Új játék" menü létrehozása
        JMenu newGame = new JMenu("Új játék");
        newGame.setFont(MENU_FONT);
        
        JMenuItem local=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                newGame(GameType.LOCAL);
            }
        });
        local.setText("Helyi játék");
        local.setFont(MENU_FONT);
        newGame.add(local);
        JMenuItem host=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                newGame(GameType.HOST);
            }
        });
        host.setText("Játék hostolása");
        host.setFont(MENU_FONT);
        newGame.add(host);
        JMenuItem join=new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                newGame(GameType.JOIN);
            }
        });
        join.setText("Csatlakozás játékhoz");
        join.setFont(MENU_FONT);
        newGame.add(join);
        
        JMenu boardSize=new JMenu("Táblaméret");
        boardSize.setFont(MENU_FONT);
        ButtonGroup group=new ButtonGroup();
        // Új játék indító menüpontok létrehozása és beállítása
        JRadioButtonMenuItem x6 = new JRadioButtonMenuItem();
        x6.setSelected(true);
        x6.setText("6x6");
        x6.setFont(MENU_FONT);
        JRadioButtonMenuItem x8 = new JRadioButtonMenuItem();
        x8.setText("8x8");
        x8.setFont(MENU_FONT);
        JRadioButtonMenuItem x10 = new JRadioButtonMenuItem();
        x10.setText("10x10");
        x10.setFont(MENU_FONT);
        
        // Menüpontok hozzáadása a menühöz
        group.add(x6);
        group.add(x8);
        group.add(x10);
        boardSize.add(x6);
        boardSize.add(x8);
        boardSize.add(x10);
        // Menü hozzáadása a menüsorhoz
        mainMenu.add(newGame);
        mainMenu.add(boardSize);
        // Az ablak menüsorának beállítása
        setJMenuBar(mainMenu);
        
        mainLabel=new JLabel("Kezdj új játékot");
        mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainLabel.setFont(MAIN_FONT);
        // Főpanel létrehozása, ezt cseréli le a játékpanel
        mainPanel=new JPanel(new BorderLayout());
        // Info szöveg hozzáadása a főpanelhez
        mainPanel.add(mainLabel, BorderLayout.CENTER);
        // Főpanel hozzáadása az ablakhoz
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }
    /*
        
    */
    public void addPanel(JPanel newPanel){
        mainPanel.setVisible(false);
        add(newPanel);
    }
    /*
        Visszatérünk a főmenüre
    */
    public void endGame(JPanel oldPanel, Player p){
        remove(oldPanel);
        switch(p){
            case RED:
                mainLabel.setText("Győztes: Piros játékos");
                mainPanel.setBackground(Color.RED);
                break;
            case GREEN:
                mainLabel.setText("Győztes: Zöld játékos");
                mainPanel.setBackground(Color.GREEN);
                break;
            case NONE:
                mainLabel.setText("Egy játékos kilépett. Kezdj új játékot!");
                mainPanel.setBackground(Color.WHITE);
                break;
        }
        mainPanel.setVisible(true);
        enableNewGame(true);
        c.close();
        c=null;
    }
    private void newGame(GameType gt){
        // a két boardsize lehet eltérhet
        int boardSize=getBoardSize();
        if(boardSize != 0){
            if(gt==GameType.LOCAL){
                addPanel(new GamePanel(MainWindow.this,gt,null,boardSize));
                enableNewGame(false);
            }else{
            try {
                    new ConnectionDialog(MainWindow.this,gt);
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void setConnection(Connection c, GameType gt){
        this.c=c;
        if(c!=null){
            int boardSize=getBoardSize();
            if(c.isServer()){
                c.sendLine(""+boardSize);
            }else{
                try {
                    boardSize=c.processBoardSize();
                } catch (IOException ex) {
                    Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            addPanel(new GamePanel(MainWindow.this,gt, c,boardSize));
            enableNewGame(false);
        }
    }
    
    /*
     * A paraméterül kapott logikai értéktől függően
     * engedélyezi vagy letiltja az új játék kezdő menüt
    át kellene írni FLIPMENU-re, és betenni egy játékból kilépés menüitemet
     */
    private void enableNewGame(boolean b){
        for(int i=0;i<mainMenu.getMenuCount();++i)
            mainMenu.getMenu(i).setEnabled(b);
    }
    private int getBoardSize(){
        int sizes[]=new int[]{6,8,10};
        JMenu m = mainMenu.getMenu(1);
        for(int i=0;i< m.getItemCount();++i){
            if(((JRadioButtonMenuItem)(m.getMenuComponent(i))).isSelected())
                return sizes[i];
        }
        return 0;
    }
    
    private WindowListener exitOperation=new WindowAdapter(){
        @Override
        public void windowClosing(WindowEvent e) {
            if(c != null){
                c.sendLine("EXIT");
            }
            System.exit(0);
        }
    };
}


