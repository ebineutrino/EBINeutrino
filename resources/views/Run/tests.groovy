package Run

import Run.tests.CompanyTest
import Run.tests.ContactTest

import javax.swing.*

def execTests(nmsp) {

    boolean done = false;

    switch (nmsp) {
        case "Company":
            system.getMainFrame().requestFocusInWindow();
            SwingUtilities.invokeLater {
                system.getMainFrame().requestFocusInWindow();
                done = new CompanyTest(system, this).done();
            }
            break;
        case "Contact":
            SwingUtilities.invokeLater {
                system.getMainFrame().requestFocusInWindow();
                done = new ContactTest(system, this).done();
            }
            break;

        case "Address":
            SwingUtilities.invokeLater {
                sleep(2000);
                system.container.setSelectedTab(3);
                system.getMainFrame().requestFocusInWindow();
                system.message.info("address test started!");
                //done = new ContactTest(system, this).done();
                //execTests("Address");
            }
            break;

    }
    return done;
}

execTests(namespace);
system.getMainFrame().requestFocusInWindow();
//execTests("Contact");