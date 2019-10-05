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
@Table(name = "COMPANYACTIVITIESDOCS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyactivitiesdocs.findAll", query = "SELECT c FROM Companyactivitiesdocs c"),
    @NamedQuery(name = "Companyactivitiesdocs.findByActivitydocid", query = "SELECT c FROM Companyactivitiesdocs c WHERE c.activitydocid = :activitydocid"),
    @NamedQuery(name = "Companyactivitiesdocs.findByName", query = "SELECT c FROM Companyactivitiesdocs c WHERE c.name = :name"),
    @NamedQuery(name = "Companyactivitiesdocs.findByCreateddate", query = "SELECT c FROM Companyactivitiesdocs c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyactivitiesdocs.findByCreatedfrom", query = "SELECT c FROM Companyactivitiesdocs c WHERE c.createdfrom = :createdfrom")})
public class Companyactivitiesdocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACTIVITYDOCID")
    private Integer activitydocid;
    @Column(name = "NAME")
    private String name;
    @Lob
    @Column(name = "FILES")
    private byte[] files;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @JoinColumn(name = "ACTIVITYID", referencedColumnName = "ACTIVITYID")
    @ManyToOne
    private Companyactivities companyactivities;

    public Companyactivitiesdocs() {
    }

    public Companyactivitiesdocs(Integer activitydocid) {
        this.activitydocid = activitydocid;
    }

    public Integer getActivitydocid() {
        return activitydocid;
    }

    public void setActivitydocid(Integer activitydocid) {
        this.activitydocid = activitydocid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFiles() {
        return files;
    }

    public void setFiles(byte[] files) {
        this.files = files;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public String getCreatedfrom() {
        return createdfrom;
    }

    public void setCreatedfrom(String createdfrom) {
        this.createdfrom = createdfrom;
    }

    public Companyactivities getCompanyactivities() {
        return companyactivities;
    }

    public void setCompanyactivities(Companyactivities companyactivities) {
        this.companyactivities = companyactivities;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activitydocid != null ? activitydocid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyactivitiesdocs)) {
            return false;
        }
        Companyactivitiesdocs other = (Companyactivitiesdocs) object;
        if ((this.activitydocid == null && other.activitydocid != null) || (this.activitydocid != null && !this.activitydocid.equals(other.activitydocid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyactivitiesdocs[ activitydocid=" + activitydocid + " ]";
    }
    
}
