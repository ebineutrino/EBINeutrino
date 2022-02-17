import javax.swing.*
import java.awt.*

JPanel panl = system.builder.getPanel("taskGraph","Project");

Point pt = panl.getLocation();
Dimension sz = panl.getSize();

system.builder.button("openView","Project").
        setLocation(system.builder.vpanel("Project").getWidth()
                -system.builder.button("openView","Project").getWidth()-20,
                            system.builder.button("openView","Project").getY());

JLabel lb = new JLabel(system.i18n("EBI_LANG_FULLMODE_VIEW"));

system.builder.vpanel("Project").add(lb,null);

lb.setVisible(false);

system.builder.button("openView","Project").actionPerformed={
 
    JFrame frm = new JFrame()
    
    frm.setTitle(system.i18n("EBI_LANG_C_TAB_PROJECT"))
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
          system.builder.vpanel("Project").revalidate()
          system.builder.vpanel("Project").add(panl,null)
          system.builder.vpanel("Project").updateUI()
    }
}