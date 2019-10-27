package org.modules.utils;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class EBISearchTreeNodeProduct extends DefaultMutableTreeTableNode {
			
		    private String productID;
		    private String productNr;
		    private String productName;
		    private String category;
		    private String type;		
			       
			    public EBISearchTreeNodeProduct(final String productID, final String productNr, final String productName, final String category, final String type) {
			    
			    	   this.productID = productID;
			    	   this.productNr = productNr;
			    	   this.productName = productName;
			    	   this.category = category;
			    	   this.type = type;
			    	   
			    }

				public String getCategory() {
					return category;
				}

				public void setCategory(final String category) {
					this.category = category;
				}


				public String getProductID() {
					return productID;
				}




				public void setProductID(final String productID) {
					this.productID = productID;
				}




				public String getProductName() {
					return productName;
				}




				public void setProductName(final String productName) {
					this.productName = productName;
				}




				public String getProductNr() {
					return productNr;
				}




				public void setProductNr(final String productNr) {
					this.productNr = productNr;
				}




				public String getType() {
					return type;
				}




				public void setType(final String type) {
					this.type = type;
				}


			}

