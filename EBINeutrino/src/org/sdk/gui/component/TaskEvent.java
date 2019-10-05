package org.sdk.gui.component;

import org.sdk.EBISystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TaskEvent extends JComponent implements MouseListener, MouseMotionListener, KeyListener {
    private String name="";
    private long id = -1;
    private final List<TaskEvent> parents = new ArrayList<TaskEvent>();
    private int duration=0;
    private int reached =0;
    private String description="";
    private Color backgroundColor = new Color(120,20,255);
    private boolean startDrag = false;
    private boolean moveEvent = false;
    private boolean resizeEvent = false;
    private int offsetX = 0;
    private int offsetY = 0;
    private String status="";
    private String type="";
    private final JPopupMenu popUp = new JPopupMenu();
    public JMenuItem relationFrom = null;
    public JMenuItem relationTo = null;
    private boolean isControl = false;

    private EBIGRCManagement container = null;

    public TaskEvent(final EBIGRCManagement cont){
        this.container = cont;
        setOpaque(true);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);

        final String relationText = EBISystem.i18n("EBI_LANG_RELATION_TO");
        relationFrom = new JMenuItem(relationText);
        relationFrom.addActionListener(new ActionListener(){
            @Override
			public void actionPerformed(final ActionEvent event){

                container.selectedRelationTask = getId();
                relationFrom.setText(relationFrom.getText()+" * ");
            }
        });
        popUp.add(relationFrom);
        
        relationTo = new JMenuItem(EBISystem.i18n("EBI_LANG_RELATION_FROM"));
        relationTo.setEnabled(false);
        relationTo.addActionListener(new ActionListener(){
            @Override
			public void actionPerformed(final ActionEvent event){
               container.getEventListener().createRelation(container.selectedRelationTask,getId());
               container.getAvailableTasks().get(container.selectedRelationTask).setParent(TaskEvent.this);
               container.getAvailableTasks().get(container.selectedRelationTask).relationFrom.setText(relationText);

               container.selectedRelationTask = -1;
               relationTo.setEnabled(false); 
               container.repaint();
            }
        });
        popUp.add(relationTo);

        final JMenuItem edit = new JMenuItem(EBISystem.i18n("EBI_LANG_EDIT_TASK"));
        edit.addActionListener(new ActionListener(){
            @Override
			public void actionPerformed(final ActionEvent event){

               if(container.getEventListener().editTaskAction(TaskEvent.this, true)){
                    repaint();
               }

            }
        });
        popUp.add(edit);
        popUp.addSeparator();
        final JMenuItem delete = new JMenuItem(EBISystem.i18n("EBI_LANG_DELETE_TASK"));
        delete.addActionListener(new ActionListener(){
            @Override
			public void actionPerformed(final ActionEvent event){
             if(container.getEventListener().deleteTaskAction(TaskEvent.this)){   
               if(container.getAvailableTasks().get(getId()) != null){
                  container.getAvailableTasks().remove(getId());
                  setVisible(false);
                  container.repaint(); 
               }
             }
            }
        });
        popUp.add(delete);
    }


    @Override
	public void paintComponent(final Graphics gs){
        super.paintComponents(gs);
        
        final Graphics2D g2  = (Graphics2D) gs;
        final BufferedImage buffImgHeader = new BufferedImage(getWidth(), getHeight(),BufferedImage.TYPE_INT_ARGB);
        
        final Graphics2D gbiHeader = buffImgHeader.createGraphics();
        gbiHeader.setStroke(new BasicStroke(1.0f));
        
        final Color startColor = this.getBackgroundColor().brighter();
        final Color endColor = this.getBackgroundColor().darker();

        // A non-cyclic gradient
        final GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, 10, endColor);

        gbiHeader.setPaint(gradient);
        gbiHeader.fill(new RoundRectangle2D.Double(0, 2, getWidth(), 17, 5, 5));
        g2.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        // paint
        g2.drawImage(buffImgHeader, null, 0, 0);
        g2.setPaint(new Color(255,255,255));

        String name = getName();
        final FontMetrics fm = g2.getFontMetrics();
        int i = 0;
        if(fm.stringWidth(name) > getWidth() - 25 ){
            while(i != name.length()){
                if(fm.stringWidth(name.substring(0,i)) > getWidth() -25){
                    name = name.substring(0,i)+"...";
                    break;
                }
              i++;
            }
        }
        g2.drawString(name,5,15);
        g2.dispose();
    }

    public void setId(final long id){
        this.id = id;
    }

    public long getId(){
        return this.id;
    }

    public List<TaskEvent> getParents() {
        return parents;
    }

    public void setParent(final TaskEvent event) {
        this.parents.add(event);
    }

    public void setBackgroundColor(final Color color){
      this.backgroundColor = color;
    }

    public Color getBackgroundColor(){
        return this.backgroundColor;
    }

    @Override
	public String getName() {
        return name;
    }

    @Override
	public void setName(final String name) {
        this.name = name;
        //this.setText(name);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration / 20;
        setSize(duration, 20);
    }

    public int getReached() {
        return reached;
    }

    public void setReached(final int reached) {
        this.reached = reached;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
        setToolTipText(description);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    @Override
	public void mouseClicked(final java.awt.event.MouseEvent mouseEvent){
        if(mouseEvent.getClickCount() >= 2){
          container.getEventListener().editTaskAction(TaskEvent.this, true);
          repaint();
        }
    }

    @Override
	public void mousePressed(final java.awt.event.MouseEvent mouseEvent){
         
         moveEvent = true;
         offsetX  =  mouseEvent.getX();
         offsetY  =  mouseEvent.getY();
         this.container.repaint();

         if(mouseEvent.getX() <= getWidth()-10 ){
            setCursor(new Cursor(Cursor.MOVE_CURSOR));
            getParent().setCursor(new Cursor(Cursor.MOVE_CURSOR));

         }
        
         if(mouseEvent.getButton() == MouseEvent.BUTTON3){
            if(container.selectedRelationTask != -1){
                relationTo.setEnabled(true);
            }
            popUp.show((JComponent)mouseEvent.getSource(),mouseEvent.getX(),mouseEvent.getY());
         }
        
    }

    @Override
	public void mouseReleased(final java.awt.event.MouseEvent mouseEvent){

        if(resizeEvent &&  startDrag){
            //setDuration((getWidth() / 20));
            resizeEvent = false;
            container.getEventListener().editTaskAction(TaskEvent.this, false);
        }else if(moveEvent &&  startDrag){
            container.getEventListener().editTaskAction(TaskEvent.this, false);
            moveEvent = false;
        }else if(isControl){

           if(this.container.selectedRelationTask  == -1){
               this.container.selectedRelationTask = this.getId();
           }else{
               container.getEventListener().createRelation(container.selectedRelationTask,getId());
               this.container.getAvailableTasks().get(this.container.selectedRelationTask).setParent(this);
               this.container.selectedRelationTask = -1;
               repaint();
           }

        }

        startDrag = false;
        this.container.taskSelected = true;
        this.container.x = getX();
        this.container.y = getY();
        this.container.width = getWidth();
        this.container.repaint();
        repaint();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); 
        getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if(mouseEvent.getButton() == MouseEvent.BUTTON3){
            popUp.show((JComponent)mouseEvent.getSource(),mouseEvent.getX(),mouseEvent.getY());
        }
    }

    @Override
	public void mouseEntered(final java.awt.event.MouseEvent mouseEvent){}

    @Override
	public void mouseExited(final java.awt.event.MouseEvent mouseEvent){
        if(!startDrag){
         setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
         getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        
    }

    @Override
	public void mouseDragged(final java.awt.event.MouseEvent mouseEvent){
       int x  =   mouseEvent.getX();
       int y  =   mouseEvent.getY();
       if(resizeEvent){
            if(x <= 20 && y <= 40 ){
              x = 0;
              y = 40;
            }else if(x <= 20 && y > 20){
               x = 0;
               y = y - (y % 20);
            }else if(y <=40 && x > 20){
               y = 40;
               x = x - (x % 20);
            }else{
              y = y - (y % 20);
              x = x - (x % 20);
            }
            if(x >= 20){
              setDuration(x);
            }
            
       }else if(moveEvent){
         final Point pt = SwingUtilities.convertPoint(this, x, y, getParent());
         int posX =  pt.x - offsetX;
         int posY =  pt.y - offsetY;
         if(posX <= 20 && posY <= 40 ){
              posX = 0;
              posY = 40;
            }else if(posX <= 20 && posY > 20){
               posX = 0;
               posY = posY - (posY % 20);
            }else if(posY <=40 && posX > 20){
               posY = 40;
               posX = posX - (posX % 20);
            }else{
              posY = posY - (posY % 20);
              posX = posX - (posX % 20);
            }
         setLocation(posX, posY);
         
       }
     if(!startDrag){
         startDrag = true;
     }
     this.container.repaint();
    }

    @Override
	public void mouseMoved(final java.awt.event.MouseEvent mouseEvent){
        
        if(mouseEvent.getX() >= getWidth()-10){
           setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
           getParent().setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
           resizeEvent = true;
           moveEvent = false;
        }else{
          resizeEvent = false;
          moveEvent = true;
          setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          getParent().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          requestFocus();
        }
    }

    @Override
	public void keyTyped(final java.awt.event.KeyEvent keyEvent){
       
    }

    @Override
	public void keyPressed(final java.awt.event.KeyEvent keyEvent){
        if(keyEvent.getKeyCode() == KeyEvent.VK_CONTROL){
            isControl = true;
        }
    }

    @Override
	public void keyReleased(final java.awt.event.KeyEvent keyEvent){
        isControl = false;
    }

}
