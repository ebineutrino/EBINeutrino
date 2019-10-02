package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "MAIL_ACCOUNT", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MailAccount.findAll", query = "SELECT m FROM MailAccount m"),
    @NamedQuery(name = "MailAccount.findById", query = "SELECT m FROM MailAccount m WHERE m.id = :id"),
    @NamedQuery(name = "MailAccount.findByAccountname", query = "SELECT m FROM MailAccount m WHERE m.accountname = :accountname"),
    @NamedQuery(name = "MailAccount.findByFolderName", query = "SELECT m FROM MailAccount m WHERE m.folderName = :folderName"),
    @NamedQuery(name = "MailAccount.findByDeleteMessage", query = "SELECT m FROM MailAccount m WHERE m.deleteMessage = :deleteMessage"),
    @NamedQuery(name = "MailAccount.findBySmtpServer", query = "SELECT m FROM MailAccount m WHERE m.smtpServer = :smtpServer"),
    @NamedQuery(name = "MailAccount.findBySmtpUser", query = "SELECT m FROM MailAccount m WHERE m.smtpUser = :smtpUser"),
    @NamedQuery(name = "MailAccount.findBySmtpPassword", query = "SELECT m FROM MailAccount m WHERE m.smtpPassword = :smtpPassword"),
    @NamedQuery(name = "MailAccount.findByEmailadress", query = "SELECT m FROM MailAccount m WHERE m.emailadress = :emailadress"),
    @NamedQuery(name = "MailAccount.findByPopServer", query = "SELECT m FROM MailAccount m WHERE m.popServer = :popServer"),
    @NamedQuery(name = "MailAccount.findByPopUser", query = "SELECT m FROM MailAccount m WHERE m.popUser = :popUser"),
    @NamedQuery(name = "MailAccount.findByPopPassword", query = "SELECT m FROM MailAccount m WHERE m.popPassword = :popPassword"),
    @NamedQuery(name = "MailAccount.findByEmailsTitle", query = "SELECT m FROM MailAccount m WHERE m.emailsTitle = :emailsTitle"),
    @NamedQuery(name = "MailAccount.findByCreatefrom", query = "SELECT m FROM MailAccount m WHERE m.createfrom = :createfrom"),
    @NamedQuery(name = "MailAccount.findByCreatedate", query = "SELECT m FROM MailAccount m WHERE m.createdate = :createdate")})
public class MailAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ACCOUNTNAME")
    private String accountname;
    @Column(name = "FOLDER_NAME")
    private String folderName;
    @Column(name = "DELETE_MESSAGE")
    private Boolean deleteMessage;
    @Column(name = "SMTP_SERVER")
    private String smtpServer;
    @Column(name = "SMTP_USER")
    private String smtpUser;
    @Column(name = "SMTP_PASSWORD")
    private String smtpPassword;
    @Column(name = "EMAILADRESS")
    private String emailadress;
    @Column(name = "POP_SERVER")
    private String popServer;
    @Column(name = "POP_USER")
    private String popUser;
    @Column(name = "POP_PASSWORD")
    private String popPassword;
    @Column(name = "EMAILS_TITLE")
    private String emailsTitle;
    @Column(name = "CREATEFROM")
    private String createfrom;
    @Column(name = "CREATEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdate;

    public MailAccount() {}

    public MailAccount(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Boolean getDeleteMessage() {
        return deleteMessage;
    }

    public void setDeleteMessage(Boolean deleteMessage) {
        this.deleteMessage = deleteMessage;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public String getSmtpUser() {
        return smtpUser;
    }

    public void setSmtpUser(String smtpUser) {
        this.smtpUser = smtpUser;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public String getEmailadress() {
        return emailadress;
    }

    public void setEmailadress(String emailadress) {
        this.emailadress = emailadress;
    }

    public String getPopServer() {
        return popServer;
    }

    public void setPopServer(String popServer) {
        this.popServer = popServer;
    }

    public String getPopUser() {
        return popUser;
    }

    public void setPopUser(String popUser) {
        this.popUser = popUser;
    }

    public String getPopPassword() {
        return popPassword;
    }

    public void setPopPassword(String popPassword) {
        this.popPassword = popPassword;
    }

    public String getEmailsTitle() {
        return emailsTitle;
    }

    public void setEmailsTitle(String emailsTitle) {
        this.emailsTitle = emailsTitle;
    }

    public String getCreatefrom() {
        return createfrom;
    }

    public void setCreatefrom(String createfrom) {
        this.createfrom = createfrom;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MailAccount)) {
            return false;
        }
        MailAccount other = (MailAccount) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.MailAccount[ id=" + id + " ]";
    }
    
}
