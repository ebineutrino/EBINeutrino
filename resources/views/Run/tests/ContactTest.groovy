package Run.tests

import java.awt.*
import java.awt.event.KeyEvent

class ContactTest{
    boolean done = false;
    ContactTest(def system, def context){
 
        //context.genderTex.selectedIndex = 2;
         

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