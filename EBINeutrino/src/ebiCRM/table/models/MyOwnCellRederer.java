package ebiCRM.table.models;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class MyOwnCellRederer extends DefaultTableCellRenderer implements TableCellRenderer{
	
	    private boolean isOffer = false;
        private boolean isActivity = false;
	   
		public MyOwnCellRederer(final boolean isOffer, final boolean isActivity){
			super();
			setHorizontalAlignment( SwingConstants.CENTER );
			setOpaque(true); //MUST do this for background to show up.
			this.isOffer = isOffer;
            this.isActivity = isActivity;
		}


		@Override
		public Component getTableCellRendererComponent(final JTable table,
														 final Object sample,
														 final boolean isSelected,
														 final boolean hasFocus,
														 final int row,
														 final int column)
		{      

            final JPanel lab = new JPanel();
            final JLabel lax = new JLabel(sample == null ? "" : (String)sample.toString());
            lab.setLayout(new BorderLayout());
            lab.add(lax,BorderLayout.CENTER);
            
            if(isActivity){
                int r = 255;
                int g = 255;
                int b = 255;

                if(table.getValueAt(row,4) != null && !"".equals(table.getValueAt(row,4).toString())){

                    final String[] splCol = table.getValueAt(row,4).toString().split(",");
                    r = Integer.parseInt(splCol[0]);
                    g = Integer.parseInt(splCol[1]);
                    b = Integer.parseInt(splCol[2]);
                }

                if(isSelected){
                    lab.setBackground(new Color(0,0,0));
                    lax.setForeground(new Color(255,255,255));
                }else{
                  lab.setBackground(new Color(r,g,b));
                  lax.setForeground(new Color(0,0,0));
                }
     
            }else{

			 String s  = null;
	          if(isOffer == true){
	        	 final MyTableModelOffer  tabModel =  (MyTableModelOffer)table.getModel(); 
	        	 s = tabModel.data[row][6].toString();
	          }else{
		         final MyTableModelOrder  tabModel =  (MyTableModelOrder)table.getModel(); 
		         s = tabModel.data[row][6].toString();
	          }
		    
			         if(!isSelected){ 
				      if(s.equalsIgnoreCase("1")) {
				          lab.setForeground(java.awt.Color.BLACK);
				          lab.setBackground(new java.awt.Color(60,40,255));
				      } else if(s.equalsIgnoreCase("0")){
				    	  lab.setForeground(java.awt.Color.BLACK);
				          lab.setBackground(java.awt.Color.green);
				      }else{
				    	  lab.setForeground(java.awt.Color.BLACK);
				    	  lab.setBackground(java.awt.Color.WHITE);
				      }
			         }else{
				    	  lab.setForeground(java.awt.Color.BLACK);
				    	  lab.setBackground(java.awt.Color.WHITE);
			         }

            }
			return lab;
		}
		
	}