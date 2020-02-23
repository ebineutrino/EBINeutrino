package Run

import Run.mail.SMTPSend
import org.sdk.EBISystem
import org.sdk.model.hibernate.MailAccount
import org.hibernate.query.Query

system.hibernate().openHibernateSession("EMAIL_SETTINGS");
system.hibernate().transaction("EMAIL_SETTINGS").begin();

Query query = system.hibernate().session("EMAIL_SETTINGS").createQuery("from MailAccount where createfrom=?1");
query.setParameter(1, EBISystem.ebiUser);

final Iterator iter = query.list().iterator();
//If you are using gmail, you need to go to https://myaccount.google.com/security scroll to the bottom and turn "Allow less secure apps: ON".
if (iter.hasNext()) {
    boolean send = false;
    try {
        SMTPSend sendEMail = new SMTPSend(system);
        MailAccount mailAccount = (MailAccount) iter.next();
        sendEMail.setSmtpEMailUser(mailAccount.getSmtpUser());
        sendEMail.setSmtpPassword(mailAccount.getSmtpPassword());
        sendEMail.setSmtpHost(mailAccount.getSmtpServer());
	 
        send = sendEMail.sendMessage(_TO, _SUBJECT, _BODY, _ATTACHMENT);
    }catch(Exception ex) {
        ex.printStackTrace();
        system.message.error(ex.getCause()+" : "+ex.getMessage());
    }catch(java.net.UnknownHostException ex) {
        ex.printStackTrace();
        system.message.error(ex.getCause()+" : "+ex.getMessage());
    }catch(com.sun.mail.util.MailConnectException ex) {
        ex.printStackTrace();
        system.message.error(ex.getCause()+" : "+ex.getMessage());
    }finally {
        if(send) {
            system.message.info("EMail successfully send!");
        }
    }
}else{
    system.message.info("Please configure an email account using system settings");
}
system.hibernate().transaction("EMAIL_SETTINGS").commit();