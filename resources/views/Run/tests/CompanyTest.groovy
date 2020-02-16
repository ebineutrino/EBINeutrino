package Run.tests

import java.awt.Robot;
import java.awt.event.KeyEvent

class CompanyTest{
    boolean done = false;
    CompanyTest(def system, def context){
        system.getMainFrame().requestFocusInWindow();

        context.custNrText.text = "custNrText test "+(new Date().getTime());
        context.internalNrText.text = "internalNrText test ";
		
        context.nameText.text = "nameText test "+(new Date().getTime());
        context.name1Text.text = "Name 1 test "+(new Date().getTime());
        context.categoryText.selectedIndex = 1;
        context.cooperationText.selectedIndex = 1;
        context.taxIDText.text = "TAXID 123456 ";
        context.employeeText.text = "10";
        context.telephoneText.text = "+39 48 439834445";
        context.faxText.text = "+39 48 439834445";
        context.internetText.text = "www.test.com";
        context.emailText.text = "test@test.com";
        context.classificationText.selectedIndex = 1;
        context.companyDescription.text = "This is a test description created from the automation script";
		
        try { 
            Thread.sleep(2000);
            system.getMainFrame().requestFocusInWindow();
            context.companyDescription.requestFocus();
            sleep(1000);
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_S);
            sleep(1000);
            robot.keyRelease(KeyEvent.VK_S);
            robot.delay(1000);
            robot.mouseMove(10, 30);
        } catch (InterruptedException e) 
        { 
            system.message.inf(e.getMessage());
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        }
        //system.container.setSelectedTab(3);
        done = true;
    }
	
    boolean done(){
        return done;	    
    }
}