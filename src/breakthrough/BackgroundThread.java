/*
 * Készítette:
 * Név: Szukács Marcell
 * Neptun-kód: ORDE4C
 * E-mail: spiglebach@gmail.com
 */
package breakthrough;

import javax.swing.SwingWorker;

public class BackgroundThread extends SwingWorker<GamePanel, GamePanel>{
    GamePanel gp;
    
    public BackgroundThread(GamePanel gp){
        super();
        this.gp=gp;
        execute();
    }
    
    @Override
    protected GamePanel doInBackground() throws Exception {
        if(!gp.over)
            gp.waitForMessage();
        else
            this.cancel(true);
        return null;
    }
    
    protected void process(GamePanel g){
        gp=g;
        execute();
    }
    
}
