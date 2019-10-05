package org.sdk.model.hibernate;

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
@Table(name = "MAIL_TEMPLATE", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MailTemplate.findAll", query = "SELECT m FROM MailTemplate m"),
    @NamedQuery(name = "MailTemplate.findById", query = "SELECT m FROM MailTemplate m WHERE m.id = :id"),
    @NamedQuery(name = "MailTemplate.findBySetfrom", query = "SELECT m FROM MailTemplate m WHERE m.setfrom = :setfrom"),
    @NamedQuery(name = "MailTemplate.findBySetdate", query = "SELECT m FROM MailTemplate m WHERE m.setdate = :setdate"),
    @NamedQuery(name = "MailTemplate.findByName", query = "SELECT m FROM MailTemplate m WHERE m.name = :name"),
    @NamedQuery(name = "MailTemplate.findByIsactive", query = "SELECT m FROM MailTemplate m WHERE m.isactive = :isactive")})
public class MailTemplate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "SETFROM")
    private String setfrom;
    @Column(name = "SETDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date setdate;
    @Column(name = "NAME")
    private String name;
    @Lob
    @Column(name = "TEMPLATE")
    private String template;
    @Column(name = "ISACTIVE")
    private Boolean isactive;

    public MailTemplate() {}

    public MailTemplate(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSetfrom() {
        return setfrom;
    }

    public void setSetfrom(String setfrom) {
        this.setfrom = setfrom;
    }

    public Date getSetdate() {
        return setdate;
    }

    public void setSetdate(Date setdate) {
        this.setdate = setdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
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
        if (!(object instanceof MailTemplate)) {
            return false;
        }
        MailTemplate other = (MailTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.MailTemplate[ id=" + id + " ]";
    }
    
}
