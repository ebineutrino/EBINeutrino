package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CRMCAMPAIGNPROP", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmcampaignprop.findAll", query = "SELECT c FROM Crmcampaignprop c"),
    @NamedQuery(name = "Crmcampaignprop.findByPropertiesid", query = "SELECT c FROM Crmcampaignprop c WHERE c.propertiesid = :propertiesid"),
    @NamedQuery(name = "Crmcampaignprop.findByCreateddate", query = "SELECT c FROM Crmcampaignprop c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmcampaignprop.findByCreatedfrom", query = "SELECT c FROM Crmcampaignprop c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmcampaignprop.findByChangeddate", query = "SELECT c FROM Crmcampaignprop c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crmcampaignprop.findByChangedfrom", query = "SELECT c FROM Crmcampaignprop c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmcampaignprop.findByName", query = "SELECT c FROM Crmcampaignprop c WHERE c.name = :name"),
    @NamedQuery(name = "Crmcampaignprop.findByValue", query = "SELECT c FROM Crmcampaignprop c WHERE c.value = :value")})
public class Crmcampaignprop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PROPERTIESID")
    private Integer propertiesid;
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
    @Column(name = "VALUE")
    private String value;
    @JoinColumn(name = "CAMPAIGNID", referencedColumnName = "CAMPAIGNID")
    @ManyToOne
    private Crmcampaign crmcampaign;

    public Crmcampaignprop() { }

    public Crmcampaignprop(Integer propertiesid) {
        this.propertiesid = propertiesid;
    }

    public Integer getPropertiesid() {
        return propertiesid;
    }

    public void setPropertiesid(Integer propertiesid) {
        this.propertiesid = propertiesid;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        hash += (propertiesid != null ? propertiesid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmcampaignprop)) {
            return false;
        }
        Crmcampaignprop other = (Crmcampaignprop) object;
        if ((this.propertiesid == null && other.propertiesid != null) || (this.propertiesid != null && !this.propertiesid.equals(other.propertiesid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmcampaignprop[ propertiesid=" + propertiesid + " ]";
    }
    
}
