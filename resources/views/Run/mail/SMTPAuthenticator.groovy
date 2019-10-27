package Run.mail

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends Authenticator{

    private String email="";
    private String password="";

    public PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(email, password);
    }
    
    public String getEmail(){
    	return this.email;
    }
    
    public void setEmail(String ml){
        this.email = ml;
    }
    
    public String getPassword(){
    	return this.password;
    }
    
    public void setPassword(String psswd){
    	this.password = psswd;
    }
}