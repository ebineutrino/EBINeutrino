package org.sdk.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "MAIL_ASSIGNED", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MailAssigned.findAll", query = "SELECT m FROM MailAssigned m"),
    @NamedQuery(name = "MailAssigned.findById", query = "SELECT m FROM MailAssigned m WHERE m.id = :id"),
    @NamedQuery(name = "MailAssigned.findByMailDate", query = "SELECT m FROM MailAssigned m WHERE m.mailDate = :mailDate"),
    @NamedQuery(name = "MailAssigned.findBySetfrom", query = "SELECT m FROM MailAssigned m WHERE m.setfrom = :setfrom"),
    @NamedQuery(name = "MailAssigned.findByMailFrom", query = "SELECT m FROM MailAssigned m WHERE m.mailFrom = :mailFrom"),
    @NamedQuery(name = "MailAssigned.findByMailTo", query = "SELECT m FROM MailAssigned m WHERE m.mailTo = :mailTo"),
    @NamedQuery(name = "MailAssigned.findByMailCc", query = "SELECT m FROM MailAssigned m WHERE m.mailCc = :mailCc"),
    @NamedQuery(name = "MailAssigned.findByMailSubject", query = "SELECT m FROM MailAssigned m WHERE m.mailSubject = :mailSubject"),
    @NamedQuery(name = "MailAssigned.findByAttachid", query = "SELECT m FROM MailAssigned m WHERE m.attachid = :attachid")})
public class MailAssigned implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "MAIL_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mailDate;
    @Column(name = "SETFROM")
    private String setfrom;
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
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;

    public MailAssigned() {}

    public MailAssigned(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
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
        if (!(object instanceof MailAssigned)) {
            return false;
        }
        MailAssigned other = (MailAssigned) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.MailAssigned[ id=" + id + " ]";
    }
    
}
