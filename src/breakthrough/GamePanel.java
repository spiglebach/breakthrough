/*
 * Készítette:
 * Név: Szukács Marcell
 * Neptun-kód: ORDE4C
 * E-mail: spiglebach@gmail.com
 */
package breakthrough;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GamePanel extends JPanel {
    // A jelenlegi játékos
    private Player currentPlayer;
    // A játékmezők tömbje
    private final GameTile[][] tiles;
    // A tábla mérete
    private final int boardSize;
    // A főablak példánya
    private final MainWindow mw;
    // A jelenlegi játékost kiíró szöveg
    private final JLabel lbPlayer;
    // A jelenlegi játékos által legutóbb kattintott mező sora és oszlopa
    private int currentTileRow;
    private int currentTileColumn;
    private GameType gt;
    private Player player=null;
    private Connection c; 
    private BackgroundThread bt;
    String lbPlayerText;
    public boolean over=false;

    GamePanel(MainWindow mw, GameType gt, Connection c, int boardSize) {
        // Alap dolgok beállítása
        super();
        this.mw = mw;
        this.boardSize = boardSize;
        this.gt=gt;
        this.c=c;
        if(c != null && c.isServer()){
            player=Player.RED;
        }else if(c != null && !c.isServer()){
            player=Player.GREEN;
        }
        currentPlayer = Player.RED;
        // Nem kattintott még saját mezőre a játékos
        currentTileRow = -1;
        currentTileColumn = -1;

        // Játékost kiíró szöveg inicializálása
        lbPlayer = new JLabel();
        lbPlayer.setFont(MainWindow.MAIN_FONT);
        lbPlayer.setHorizontalAlignment(SwingConstants.CENTER);
        updatePlayerText();

        // Tábla létrehozása a megfelelő méretekkel
        Container board = new Container();
        board.setLayout(new GridLayout(boardSize, boardSize));

        
        //Játékmezők létrehozása és betöltése a táblába
        tiles = new GameTile[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            Player p;
            if (i < 2) {
                p = Player.RED;
            } else if (i < boardSize - 2) {
                p = Player.NONE;
            } else {
                p = Player.GREEN;
            }
            for (int j = 0; j < boardSize; j++) {
                tiles[i][j] = new GameTile(p,i,j);
                // Piros játékos mezőinek ellátása eseménykezelőkkel
                if (tiles[i][j].getPlayer() == Player.RED && (player==null || player== Player.RED)) {
                    tiles[i][j].addActionListener(select(i, j));
                }
                board.add(tiles[i][j]);
            }
        }
        
        // Főpanel feltöltése a létrehozott elemekkel
        setLayout(new BorderLayout(20, 20));
        add(lbPlayer, BorderLayout.PAGE_START);
        add(board, BorderLayout.CENTER);
        setVisible(true);
        //ez késlelteti a panel megjelnését, a mainwindowban kellene meghívni
        //waitForMessage();
        /*BackgroundThread bt=new BackgroundThread();
        bt.process(getInstanceList());
        bt.execute();*/
        bt=new BackgroundThread(this);
    }
    
    public void waitForMessage(){
        if(player != null && currentPlayer != player ){
            
                    System.out.println("nem kapjuk meg a számokat");
            int fromto[]=null;
            while(fromto==null){
                try {
                    fromto=c.processLine();
                } catch (IOException ex) {
                    Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NumberFormatException ex) {
                    Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExitException ex) {
                    mw.endGame(this, Player.NONE);
                }
            }
            System.out.println("számok megkapva "+currentPlayer+"-től");
            JButton fakeBtn=new JButton();
            fakeBtn.addActionListener(move(tiles[fromto[0]][fromto[1]],tiles[fromto[2]][fromto[3]]));
            fakeBtn.doClick();
        }
    }

    /*
        Megváltoztatja a jelenlegi játékost, kiírja a színét,
        a mezőkről letörli az eseménykezelőket, majd az új jelenlegi
        játékos mezőit ellátja eseménykezelőkkel
    */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == Player.RED ? Player.GREEN : Player.RED);
        updatePlayerText();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                tiles[i][j].removeActionListeners();
                tiles[i][j].resetBorder();
                // ha lokális a játék vagy ha a mi színünk jön
                if (tiles[i][j].getPlayer() == currentPlayer && (player==null || player== currentPlayer)) {
                    tiles[i][j].addActionListener(select(i, j));
                }
            }
        }
    }

    /*
        A jelenlegi játékos mezőin található eseménykezelő.
        Ha már korábban választott egy mezőt a játékos, akkor az
        arról a mezőről tehető lépéseket eltűnteti, az újonnan
        választott mezőről tehető lépéseket pedig hozzáadja
    */
    private ActionListener select(int i, int j) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (currentTileRow != -1) {
                    removePossibleStepsFrom(currentTileRow, currentTileColumn);
                }
                currentTileRow = i;
                currentTileColumn = j;
                addPossibleStepsFrom(currentTileRow, currentTileColumn);
            }
        };
    }

    /*
        A lejenlegi játékos által kiválasztott saját mezőről elérhető
        nem-saját mezőkön található eseménykezelő
        1, az első paraméterként kapott mezőt játékos nélkülivé-,
        2, a második paraméterként kapott mezőt a jelenlegi játékosévá teszi
        3, megnézi, hogy nyert-e valaki
        4, játékost vált
    */
    private ActionListener move(GameTile from, GameTile to) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                to.setPlayer(currentPlayer);
                from.setPlayer(Player.NONE);
                if(player != null && player==currentPlayer){
                    String line=""+from.row +","+from.column+","+to.row+","+to.column;
                    c.sendLine(line);
                    System.out.println("számok elküldve:"+currentPlayer+"-től");
                }
                currentTileRow = -1;
                currentTileColumn = -1;

                checkWinner();

                switchPlayer();
                
                bt= new BackgroundThread(GamePanel.this);
            }
        };
    }

    /*
        Ellenőrzi, hogy nyert-e valamelyik játékos,
        ha igen, értesíti a főablakot
    */
    private void checkWinner() {
        for (int i = 0; i < boardSize; i++) {
            if (tiles[0][i].getPlayer() == Player.GREEN) {
                mw.endGame(this, Player.GREEN);
                over=true;
            }
            if (tiles[boardSize - 1][i].getPlayer() == Player.RED) {
                mw.endGame(this, Player.RED);
                over=true;
            }
        }
    }

    /*
        A paraméterül kapott sorban és oszlopban lévő mezőről elérhető
        nem-saját mezőkről letörli az eseménykezelőket és
        visszaállítja a körvonalukat
    */
    private void removePossibleStepsFrom(int i, int j) {
        tiles[i][j].resetBorder();
        ArrayList<GameTile> possibleSteps = getPossibleStepsFrom(i, j);
        for (GameTile gt : possibleSteps) {
            gt.removeActionListeners();
            gt.resetBorder();
        }
    }

    /*
        Beállítja paraméterül kapott sorban és oszlopban lévő mező
        körvonalát és a róla elérhető mezőkhöz hozzáadja a megfelelő
        eseménykezelőt és beállítja a körvonalukat
    */
    private void addPossibleStepsFrom(int i, int j) {
        tiles[i][j].setBorder(GameTile.CURRENT_BORDER);
        ArrayList<GameTile> possibleSteps = getPossibleStepsFrom(i, j);
        for (GameTile gt : possibleSteps) {
            gt.addActionListener(move(tiles[i][j], gt));
            gt.setBorder(GameTile.POSSIBLE_BORDER);
        }
    }

    /*
        Játékostól függően egy listába gyűjti a paraméterül kapott
        sorban és oszlopban lévő mezőről elérhető nem-saját
        mezőket, majd visszatér a listával
    */
    private ArrayList<GameTile> getPossibleStepsFrom(int i, int j) {
        ArrayList<GameTile> possibleSteps = new ArrayList<GameTile>();
        if (currentPlayer == Player.RED) {
            if (tiles[i + 1][j].getPlayer() == Player.NONE) {
                possibleSteps.add(tiles[i + 1][j]);
            }
            if (j < boardSize - 1) {
                if (tiles[i + 1][j + 1].getPlayer() != Player.RED) {
                    possibleSteps.add(tiles[i + 1][j + 1]);
                }
            }
            if (j > 0) {
                if (tiles[i + 1][j - 1].getPlayer() != Player.RED) {
                    possibleSteps.add(tiles[i + 1][j - 1]);
                }
            }
        } else {//GREEN PLAYER******************************
            if (tiles[i - 1][j].getPlayer() == Player.NONE) {
                possibleSteps.add(tiles[i - 1][j]);
            }
            if (j < boardSize - 1) {
                if (tiles[i - 1][j + 1].getPlayer() != Player.GREEN) {
                    possibleSteps.add(tiles[i - 1][j + 1]);
                }
            }
            if (j > 0) {
                if (tiles[i - 1][j - 1].getPlayer() != Player.GREEN) {
                    possibleSteps.add(tiles[i - 1][j - 1]);
                }
            }
        }
        return possibleSteps;
    }
    
    /*
        A játékost leíró labelt az aktuális játékosnak
        és játékmódnak megfelelően frissíti
    */
    void updatePlayerText(){
        switch(currentPlayer){
            case RED:
                lbPlayerText="Piros játékos";
                lbPlayer.setForeground(Color.red);
                break;
            case GREEN:
                lbPlayerText="Zöld játékos";
                lbPlayer.setForeground(GameTile.GREEN);
                break;
            default:
                lbPlayerText="Hiba";
                break;
        }
        if(c!=null){
            if(currentPlayer.equals(player)){
                    lbPlayerText+=" (a te köröd)";
            }else{
                lbPlayerText+=" (az ellenfél köre)";
            }
        }
        lbPlayer.setText(lbPlayerText);
    }
    
}
