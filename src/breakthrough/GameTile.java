/*
 * Készítette:
 * Név: Szukács Marcell
 * Neptun-kód: ORDE4C
 * E-mail: spiglebach@gmail.com
 */
package breakthrough;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class GameTile extends JButton {
    // A Color osztályénál egy sötétebb zöld
    public static final Color GREEN=new Color(0,128,0);
    // A mezők alap körvonala, 2-es méretű fekete
    private static final LineBorder DEFAULT_BORDER=new LineBorder(Color.BLACK, 2);
    // A játékos által kiválasztott mező körvonala, 4-es méretű narancs
    public static final LineBorder CURRENT_BORDER=new LineBorder(Color.ORANGE, 4);
    // A kiválasztott csempéből elérhető mezők körvonala, 4-es méretű kék
    public static final LineBorder POSSIBLE_BORDER=new LineBorder(Color.BLUE, 4);
    // A mezőn található játékos
    private Player p;
    public int row;
    public int column;
    
    /*
        Konstruktor, létrehoz egy gombot és beállítja:
            -a játékosát
            -a színét
            -a körvonalát
    */
    public GameTile(Player player, int i, int j){
        super();
        row=i;
        column=j;
        setBorder(DEFAULT_BORDER);
        setPlayer(player);
    }
    
    public Player getPlayer(){
        return p;
    }
    
    /*
        Beállítja a mező játékosát a paraméterül kapott játékosra
        és a színét is megfelelően változtatja
    */
    public final void setPlayer(Player p){
        this.p=p;
        switch(p){
            case RED:
                setBackground(Color.red);
                break;
            case GREEN:
                setBackground(GREEN);
                break;
            default:
                setBackground(Color.white);
                break;
        }
    }
    
    /*
        Kitörli a játékmező összes eseménykezelőjét
    */
    public void removeActionListeners(){
        ActionListener[] actionListeners=getActionListeners();
        if(actionListeners != null && actionListeners.length!=0){
            for(ActionListener al : actionListeners){
                removeActionListener(al);
            }
        }
    }
    
    /*
        Visszaállítja a játékmező körvonalát az alapállapotra
    */
    public void resetBorder(){
        setBorder(DEFAULT_BORDER);
    }
}