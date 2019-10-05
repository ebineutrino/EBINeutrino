package org.sdk.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "CRMCAMPAIGN", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmcampaign.findAll", query = "SELECT c FROM Crmcampaign c"),
    @NamedQuery(name = "Crmcampaign.findByCampaignid", query = "SELECT c FROM Crmcampaign c WHERE c.campaignid = :campaignid"),
    @NamedQuery(name = "Crmcampaign.findByCampaignnr", query = "SELECT c FROM Crmcampaign c WHERE c.campaignnr = :campaignnr"),
    @NamedQuery(name = "Crmcampaign.findByCreateddate", query = "SELECT c FROM Crmcampaign c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmcampaign.findByCreatedfrom", query = "SELECT c FROM Crmcampaign c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmcampaign.findByChangeddate", query = "SELECT c FROM Crmcampaign c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crmcampaign.findByChangedfrom", query = "SELECT c FROM Crmcampaign c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmcampaign.findByName", query = "SELECT c FROM Crmcampaign c WHERE c.name = :name"),
    @NamedQuery(name = "Crmcampaign.findByStatus", query = "SELECT c FROM Crmcampaign c WHERE c.status = :status"),
    @NamedQuery(name = "Crmcampaign.findByValidfrom", query = "SELECT c FROM Crmcampaign c WHERE c.validfrom = :validfrom"),
    @NamedQuery(name = "Crmcampaign.findByValidto", query = "SELECT c FROM Crmcampaign c WHERE c.validto = :validto")})
public class Crmcampaign implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CAMPAIGNID")
    private Integer campaignid;
    @Column(name = "CAMPAIGNNR")
    private String campaignnr;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "VALIDFROM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validfrom;
    @Column(name = "VALIDTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validto;
    @OneToMany(mappedBy = "crmcampaign")
    private Set<Crmcampaignposition> crmcampaignpositionSet = new HashSet();
    @OneToMany(mappedBy = "crmcampaign")
    private Set<Crmcampaignreceiver> crmcampaignreceiverSet = new HashSet();
    @OneToMany(mappedBy = "crmcampaign")
    private Set<Crmcampaigndocs> crmcampaigndocsSet = new HashSet();
    @OneToMany(mappedBy = "crmcampaign")
    private Set<Crmcampaignprop> crmcampaignpropSet = new HashSet();

    public Crmcampaign() {
    }

    public Crmcampaign(Integer campaignid) {
        this.campaignid = campaignid;
    }

    public Integer getCampaignid() {
        return campaignid;
    }

    public void setCampaignid(Integer campaignid) {
        this.campaignid = campaignid;
    }

    public String getCampaignnr() {
        return campaignnr;
    }

    public void setCampaignnr(String campaignnr) {
        this.campaignnr = campaignnr;
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

    public Date getChangeddate() {
        return changeddate;
    }

    public void setChangeddate(Date changeddate) {
        this.changeddate = changeddate;
    }

    public String getChangedfrom() {
        return changedfrom;
    }

    public void setChangedfrom(String changedfrom) {
        this.changedfrom = changedfrom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getValidfrom() {
        return validfrom;
    }

    public void setValidfrom(Date validfrom) {
        this.validfrom = validfrom;
    }

    public Date getValidto() {
        return validto;
    }

    public void setValidto(Date validto) {
        this.validto = validto;
    }

    @XmlTransient
    public Set<Crmcampaignposition> getCrmcampaignpositions() {
        return crmcampaignpositionSet;
    }

    public void setCrmcampaignpositions(Set<Crmcampaignposition> crmcampaignpositionSet) {
        this.crmcampaignpositionSet = crmcampaignpositionSet;
    }

    @XmlTransient
    public Set<Crmcampaignreceiver> getCrmcampaignreceivers() {
        return crmcampaignreceiverSet;
    }

    public void setCrmcampaignreceivers(Set<Crmcampaignreceiver> crmcampaignreceiverSet) {
        this.crmcampaignreceiverSet = crmcampaignreceiverSet;
    }

    @XmlTransient
    public Set<Crmcampaigndocs> getCrmcampaigndocses() {
        return crmcampaigndocsSet;
    }

    public void setCrmcampaigndocs(Set<Crmcampaigndocs> crmcampaigndocsSet) {
        this.crmcampaigndocsSet = crmcampaigndocsSet;
    }

    @XmlTransient
    public Set<Crmcampaignprop> getCrmcampaignprops() {
        return crmcampaignpropSet;
    }

    public void setCrmcampaignprops(Set<Crmcampaignprop> crmcampaignpropSet) {
        this.crmcampaignpropSet = crmcampaignpropSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (campaignid != null ? campaignid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmcampaign)) {
            return false;
        }
        Crmcampaign other = (Crmcampaign) object;
        if ((this.campaignid == null && other.campaignid != null) || (this.campaignid != null && !this.campaignid.equals(other.campaignid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmcampaign[ campaignid=" + campaignid + " ]";
    }
    
}
