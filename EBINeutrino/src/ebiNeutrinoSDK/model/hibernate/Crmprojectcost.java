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
@Table(name = "CRMPROJECTCOST", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmprojectcost.findAll", query = "SELECT c FROM Crmprojectcost c"),
    @NamedQuery(name = "Crmprojectcost.findByCostid", query = "SELECT c FROM Crmprojectcost c WHERE c.costid = :costid"),
    @NamedQuery(name = "Crmprojectcost.findByCreateddate", query = "SELECT c FROM Crmprojectcost c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Crmprojectcost.findByCreatedfrom", query = "SELECT c FROM Crmprojectcost c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Crmprojectcost.findByChangeddate", query = "SELECT c FROM Crmprojectcost c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Crmprojectcost.findByChangedfrom", query = "SELECT c FROM Crmprojectcost c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Crmprojectcost.findByName", query = "SELECT c FROM Crmprojectcost c WHERE c.name = :name"),
    @NamedQuery(name = "Crmprojectcost.findByValue", query = "SELECT c FROM Crmprojectcost c WHERE c.value = :value")})
public class Crmprojectcost implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "COSTID")
    private Integer costid;
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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VALUE")
    private Double value;
    @JoinColumn(name = "TASKID", referencedColumnName = "TASKIID")
    @ManyToOne
    private Crmprojecttask crmprojecttask;

    public Crmprojectcost() {}

    public Crmprojectcost(Integer costid) {
        this.costid = costid;
    }

    public Integer getCostid() {
        return costid;
    }

    public void setCostid(Integer costid) {
        this.costid = costid;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Crmprojecttask getCrmprojecttask() {
        return crmprojecttask;
    }

    public void setCrmprojecttask(Crmprojecttask crmprojecttask) {
        this.crmprojecttask = crmprojecttask;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (costid != null ? costid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmprojectcost)) {
            return false;
        }
        Crmprojectcost other = (Crmprojectcost) object;
        if ((this.costid == null && other.costid != null) || (this.costid != null && !this.costid.equals(other.costid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmprojectcost[ costid=" + costid + " ]";
    }
    
}
