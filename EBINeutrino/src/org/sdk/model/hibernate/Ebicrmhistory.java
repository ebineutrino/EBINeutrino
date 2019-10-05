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
@Table(name = "EBICRMHISTORY", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ebicrmhistory.findAll", query = "SELECT e FROM Ebicrmhistory e"),
    @NamedQuery(name = "Ebicrmhistory.findByHistoryid", query = "SELECT e FROM Ebicrmhistory e WHERE e.historyid = :historyid"),
    @NamedQuery(name = "Ebicrmhistory.findByCompanyid", query = "SELECT e FROM Ebicrmhistory e WHERE e.companyid = :companyid"),
    @NamedQuery(name = "Ebicrmhistory.findByCategory", query = "SELECT e FROM Ebicrmhistory e WHERE e.category = :category"),
    @NamedQuery(name = "Ebicrmhistory.findByChangedfrom", query = "SELECT e FROM Ebicrmhistory e WHERE e.changedfrom = :changedfrom"),
    @NamedQuery(name = "Ebicrmhistory.findByChangeddate", query = "SELECT e FROM Ebicrmhistory e WHERE e.changeddate = :changeddate")})
public class Ebicrmhistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "HISTORYID")
    private Integer historyid;
    @Column(name = "COMPANYID")
    private Integer companyid;
    @Column(name = "CATEGORY")
    private String category;
    @Lob
    @Column(name = "CHANGEDVALUE")
    private String changedvalue;
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;

    public Ebicrmhistory() {}

    public Ebicrmhistory(Integer historyid) {
        this.historyid = historyid;
    }

    public Integer getHistoryid() {
        return historyid;
    }

    public void setHistoryid(Integer historyid) {
        this.historyid = historyid;
    }

    public Integer getCompanyid() {
        return companyid;
    }

    public void setCompanyid(Integer companyid) {
        this.companyid = companyid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getChangedvalue() {
        return changedvalue;
    }

    public void setChangedvalue(String changedvalue) {
        this.changedvalue = changedvalue;
    }

    public String getChangedfrom() {
        return changedfrom;
    }

    public void setChangedfrom(String changedfrom) {
        this.changedfrom = changedfrom;
    }

    public Date getChangeddate() {
        return changeddate;
    }

    public void setChangeddate(Date changeddate) {
        this.changeddate = changeddate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historyid != null ? historyid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ebicrmhistory)) {
            return false;
        }
        Ebicrmhistory other = (Ebicrmhistory) object;
        if ((this.historyid == null && other.historyid != null) || (this.historyid != null && !this.historyid.equals(other.historyid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Ebicrmhistory[ historyid=" + historyid + " ]";
    }
    
}
