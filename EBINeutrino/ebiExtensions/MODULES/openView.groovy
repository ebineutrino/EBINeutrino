import javax.swing.*
import java.awt.*

JPanel panl = system.gui.getPanel("taskGraph","Project")

Point pt = panl.getLocation()
Dimension sz = panl.getSize()

system.gui.button("openView","Project").
        setLocation(system.gui.vpanel("Project").getWidth()
                -system.gui.button("openView","Project").getWidth()-20,
                            system.gui.button("openView","Project").getY())

JLabel lb = new JLabel(system.getLANG("EBI_LANG_FULLMODE_VIEW"))
system.gui.vpanel("Project").add(lb,null)
lb.setVisible(false)

system.gui.button("openView","Project").actionPerformed={
 
    JFrame frm = new JFrame()
    
    frm.setTitle(system.getLANG("EBI_LANG_C_TAB_PROJECT"))
    frm.setSize(800,600)
    frm.setLocation(150,50)
    frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE)
    frm.setVisible(true)  
    
    frm.getContentPane().setLayout(new BorderLayout())
    frm.getContentPane().add(panl,BorderLayout.CENTER)
    
    lb.setVisible(true)   
    lb.setLocation(pt)
    lb.setSize(sz)
    
    frm.windowClosing={
          panl.setLocation(pt)
          panl.setSize(sz)
          lb.setVisible(false)
          system.gui.vpanel("Project").revalidate()
          system.gui.vpanel("Project").add(panl,null)
          system.gui.vpanel("Project").updateUI()
    }
}