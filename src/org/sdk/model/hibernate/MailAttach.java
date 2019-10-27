package org.sdk.model.hibernate;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "MAIL_ATTACH", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MailAttach.findAll", query = "SELECT m FROM MailAttach m"),
    @NamedQuery(name = "MailAttach.findById", query = "SELECT m FROM MailAttach m WHERE m.id = :id"),
    @NamedQuery(name = "MailAttach.findByMailAttachid", query = "SELECT m FROM MailAttach m WHERE m.mailAttachid = :mailAttachid"),
    @NamedQuery(name = "MailAttach.findByFilename", query = "SELECT m FROM MailAttach m WHERE m.filename = :filename")})
public class MailAttach implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "MAIL_ATTACHID")
    private String mailAttachid;
    @Column(name = "FILENAME")
    private String filename;
    @Lob
    @Column(name = "FILEBIN")
    private byte[] filebin;

    public MailAttach(){}

    public MailAttach(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMailAttachid() {
        return mailAttachid;
    }

    public void setMailAttachid(String mailAttachid) {
        this.mailAttachid = mailAttachid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public byte[] getFilebin() {
        return filebin;
    }

    public void setFilebin(byte[] filebin) {
        this.filebin = filebin;
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
        if (!(object instanceof MailAttach)) {
            return false;
        }
        MailAttach other = (MailAttach) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.MailAttach[ id=" + id + " ]";
    }
    
}
