package ebiNeutrino.core.update;
 
public class EBINeutrinoUpdate {

    public EBINeutrinoUpdate(){}

    public static void main(final String[] arg){
        
           /*SwingUtilities.invokeLater(new Runnable(){
               public void run(){
                    final EBISocketDownloader fileLoader = new EBISocketDownloader();
                    fileLoader.SysPath = arg[0] == null ? "" : arg[0];

                    fileLoader.setConnection();

                    if(fileLoader.readConfig()){
                      System.out.println("Avvio update");
                      final JFrame dialog = new JFrame();
                      dialog.setTitle("EBI Neutrino R1 System Update");
                      dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                      dialog.setSize(new Dimension(450,400));
                      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                      Dimension frameSize = dialog.getSize();
                      dialog.setLocation((d.width - frameSize.width) / 2, ((d.height-150) - frameSize.height) / 2);
                      dialog.setLayout(new BorderLayout());

                      final JOptionPane webBrowser = new JOptionPane();
                   

                      webBrowser.addComponentListener(new () {
                          @Override
                          public void commandReceived(WebBrowserCommandEvent e) {

                            if(e.getCommand().equals("close")){
                                  try {

                                    if(EBINeutrinoUpdate.isWindows()){
                                        System.out.println(System.getProperty("user.dir"));
                                        Runtime.getRuntime().exec("startNeutrinoWindows.bat");
                                    }else if(EBINeutrinoUpdate.isMac()){
                                        Runtime.getRuntime().exec("startNeutrinoMAC.sh");
                                    }else if(EBINeutrinoUpdate.isUnix()){
                                        Runtime.getRuntime().exec("startNeutrinoLinux.sh");
                                    }
                                    
                                } catch (IOException e1) {
                                      JOptionPane.showMessageDialog(null,e1.getMessage());
                                    e1.printStackTrace();
                                }
                                dialog.setVisible(false);
                                dialog.dispose();
                                System.exit(0);
                            }

                          }
                     });

                      final StringBuffer buffer = new StringBuffer();
                      buffer.append("<html>");
                      buffer.append( "  <head>" +
                                    "    <script language=\"JavaScript\" type=\"text/javascript\">" +
                                    "      " +
                                    "      function sendCommand(command) {" +
                                    "        window.location = 'command://'+command;" +
                                    "      }"  +
                                    "      " +
                                    "    </script>" +
                                    "  </head>" );

                      buffer.append("<body><h2>EBI Neutrino R1 System Update</h2><br>");
                      File f = new File("images/load_mini.gif");
                      final File f1 = new File("images/icon_hacken.gif");

                      buffer.append("Read file to update, Please wait...<img id=\"IDX\" src=\""+f.toURI()+"\" width=\"20\" height=\"20\" />");
                      buffer.append("<hr>");

                      dialog.add(webBrowser, BorderLayout.CENTER);
                      dialog.setVisible(true);
                      webBrowser.setHTMLContent(buffer.toString());
                      final Runnable run = new Runnable(){
                          public void run(){
                              Iterator iter = fileLoader.arrList.iterator();
                              
                              while(iter.hasNext()){

                                  try {
                                      Thread.sleep(1000);
                                  } catch (InterruptedException e) {
                                      e.printStackTrace();
                                  }

                                  String [] arrx = (String[])iter.next();

                                  buffer.append("<br> "+arrx[1]+"...");

                                  fileLoader.writeFileTo(fileLoader.retriveData(arrx[1]),new File(arrx[3]));

                                  SwingUtilities.invokeLater(new Runnable(){
                                      public void run(){
                                         buffer.append("<img src=\""+f1.toURI()+"\" width=\"20\" height=\"20\" />");
                                         webBrowser.setHTMLContent(buffer.toString());
                                      }

                                  });
                              }

                              SwingUtilities.invokeLater(new Runnable(){
                                  public void run(){
                                     buffer.append("<br><br><hr><br>");
                                     buffer.append("Press continue to restart EBI Neutrino R1...<br><br>");
                                     buffer.append("<button name=\"close\" onClick=\"sendCommand('close'); return false\" value=\"1\" >Continue</button> <br>");
                                     buffer.append("<script> document.getElementById('IDX').style.visibility='hidden';</script>");
                                     buffer.append("</body></html>");

                                     webBrowser.setHTMLContent(buffer.toString());
                                  }
                              });
                          }

                      };

                      Thread ltr = new Thread(run);
                      ltr.start();  

                    }
               }

            });
           NativeInterface.runEventPump();*/
    }

        public static boolean isWindows(){

            final String os = System.getProperty("os.name").toLowerCase();
            //windows
            return (os.indexOf( "win" ) >= 0);

        }

        public static boolean isMac(){

            final String os = System.getProperty("os.name").toLowerCase();
            //Mac
            return (os.indexOf( "mac" ) >= 0);

        }

        public static boolean isUnix(){

            final String os = System.getProperty("os.name").toLowerCase();
            //linux or unix
            return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);

        }

}
