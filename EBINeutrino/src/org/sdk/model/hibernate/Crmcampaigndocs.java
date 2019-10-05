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
@Table(name = "CRMCAMPAIGNDOCS", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmcampaigndocs.findAll", query = "SELECT c FROM Crmcampaigndocs c"),
    @NamedQuery(name = "Crmcampaigndocs.findByDocid", query = "SELECT c FROM Crmcampaigndocs c WHERE c.docid = :docid"),
    @NamedQuery(name = "Crmcampaigndocs.findByName", query = "SELECT c FROM Crmcampaigndocs c WHERE c.name = :name"),
    @NamedQuery(name = "Crmcampaigndocs.findByCreateddate", query = "SELECT c FROM Crmcampaigndocs c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmcampaigndocs.findByCreatedfrom", query = "SELECT c FROM Crmcampaigndocs c WHERE c.createdfrom = :createdfrom")})
public class Crmcampaigndocs implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "DOCID")
    private Integer docid;
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
    @JoinColumn(name = "CAMPAIGNID", referencedColumnName = "CAMPAIGNID")
    @ManyToOne
    private Crmcampaign crmcampaign;

    public Crmcampaigndocs() { }

    public Crmcampaigndocs(Integer docid) {
        this.docid = docid;
    }

    public Integer getDocid() {
        return docid;
    }

    public void setDocid(Integer docid) {
        this.docid = docid;
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

    public Crmcampaign getCrmcampaign() {
        return crmcampaign;
    }

    public void setCrmcampaign(Crmcampaign crmcampaign) {
        this.crmcampaign = crmcampaign;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (docid != null ? docid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmcampaigndocs)) {
            return false;
        }
        Crmcampaigndocs other = (Crmcampaigndocs) object;
        if ((this.docid == null && other.docid != null) || (this.docid != null && !this.docid.equals(other.docid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmcampaigndocs[ docid=" + docid + " ]";
    }
    
}
