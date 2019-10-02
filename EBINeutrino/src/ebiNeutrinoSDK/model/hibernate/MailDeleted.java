package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "MAIL_DELETED", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MailDeleted.findAll", query = "SELECT m FROM MailDeleted m"),
    @NamedQuery(name = "MailDeleted.findById", query = "SELECT m FROM MailDeleted m WHERE m.id = :id"),
    @NamedQuery(name = "MailDeleted.findByMailFrom", query = "SELECT m FROM MailDeleted m WHERE m.mailFrom = :mailFrom"),
    @NamedQuery(name = "MailDeleted.findByMailTo", query = "SELECT m FROM MailDeleted m WHERE m.mailTo = :mailTo"),
    @NamedQuery(name = "MailDeleted.findByMailCc", query = "SELECT m FROM MailDeleted m WHERE m.mailCc = :mailCc"),
    @NamedQuery(name = "MailDeleted.findByMailSubject", query = "SELECT m FROM MailDeleted m WHERE m.mailSubject = :mailSubject"),
    @NamedQuery(name = "MailDeleted.findByAttachid", query = "SELECT m FROM MailDeleted m WHERE m.attachid = :attachid"),
    @NamedQuery(name = "MailDeleted.findByMailDate", query = "SELECT m FROM MailDeleted m WHERE m.mailDate = :mailDate"),
    @NamedQuery(name = "MailDeleted.findBySetfrom", query = "SELECT m FROM MailDeleted m WHERE m.setfrom = :setfrom")})
public class MailDeleted implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "MAIL_FROM")
    private String mailFrom;
    @Column(name = "MAIL_TO")
    private String mailTo;
    @Column(name = "MAIL_CC")
    private String mailCc;
    @Column(name = "MAIL_SUBJECT")
    private String mailSubject;
    @Lob
    @Column(name = "MAIL_MESSAGE")
    private String mailMessage;
    @Column(name = "ATTACHID")
    private String attachid;
    @Column(name = "MAIL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mailDate;
    @Column(name = "SETFROM")
    private String setfrom;

    public MailDeleted() {}

    public MailDeleted(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    public String getMailCc() {
        return mailCc;
    }

    public void setMailCc(String mailCc) {
        this.mailCc = mailCc;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(String mailMessage) {
        this.mailMessage = mailMessage;
    }

    public String getAttachid() {
        return attachid;
    }

    public void setAttachid(String attachid) {
        this.attachid = attachid;
    }

    public Date getMailDate() {
        return mailDate;
    }

    public void setMailDate(Date mailDate) {
        this.mailDate = mailDate;
    }

    public String getSetfrom() {
        return setfrom;
    }

    public void setSetfrom(String setfrom) {
        this.setfrom = setfrom;
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
        if (!(object instanceof MailDeleted)) {
            return false;
        }
        MailDeleted other = (MailDeleted) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.MailDeleted[ id=" + id + " ]";
    }
    
}
