package Run.mail

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*

//If you are using gmail, you need to go to https://myaccount.google.com/security scroll to the bottom and turn ON "Allow less secure apps: ON".
class SMTPSend{

    Properties props = new Properties();
    SMTPAuthenticator auth = new SMTPAuthenticator();
    Session session = null;
    def system
     
    String smtpEMailUser = ""; // ex email@gmail.com
    String smtpPassword = ""; // password111
    String smtpHost = ""; // smtp.gmail.com
    int smtpPort = 465; //465,587
	
    SMTPSend(def sys){
        system = sys;
    }
    
    void configure(){
        props.put("mail.smtp.user", smtpEMailUser);
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
		
        session = Session.getInstance(props, auth)
        session.setDebug(true);
    }
    
	
    boolean sendMessage(String to, String subject, String message, String fileName){
        boolean ret = true;
        try {
             
            def msg = new MimeMessage(session);
		
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(to));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(message);
            messageBodyPart.setContent(message, "text/html; charset=utf-8");
        
            // Create a multipar message
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart); 
            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(new File(fileName).getName());
            multipart.addBodyPart(messageBodyPart);
 
            msg.setContent(multipart);
		 
            Transport transport = session.getTransport("smtps");
            transport.connect(smtpHost, smtpPort, smtpEMailUser, smtpPassword);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            
        }catch(Exception ex) {
            ex.printStackTrace();
            system.message.error(ex.getMessage());
            ret = false;
        }catch (java.net.UnknownHostException ex) {
            system.message.error(ex.getMessage());
            ex.printStackTrace();
            ret = false;
        }
        return ret;
    }
}
