package EBICRM.Run.tests

import java.awt.*
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
		context.companyDescription.text = "This is a test description created from the automation script for test";
		
		 
		system.getMainFrame().requestFocusInWindow();
		sleep(1000);
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.CTRL_MASK);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		sleep(1000);
		robot = new Robot();
		robot.keyPress(KeyEvent.CTRL_MASK);
		robot.keyPress(KeyEvent.VK_T);
		robot.keyRelease(KeyEvent.VK_T);
		system.container.setSelectedTab(3);
		done = true;
	}
	
	boolean done(){
		return done;	    
	}
}