package EBICRM.Run.tests

import java.awt.*
import java.awt.event.KeyEvent

class ContactTest{
    boolean done = false;
    ContactTest(def system, def context){

        system.getMainFrame().requestFocusInWindow();
        //context.genderTex.selectedIndex = 2;
        //context.titleText="Prf.";

        system.getMainFrame().requestFocusInWindow();
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.CTRL_MASK);
        robot.keyPress(KeyEvent.VK_S);
        robot.keyRelease(KeyEvent.VK_S);
        done = true;
    }

    boolean done(){
        return done;
    }
}