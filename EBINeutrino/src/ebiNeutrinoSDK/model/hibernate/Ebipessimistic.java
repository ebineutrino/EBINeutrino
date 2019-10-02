package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "EBIPESSIMISTIC", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ebipessimistic.findAll", query = "SELECT e FROM Ebipessimistic e"),
    @NamedQuery(name = "Ebipessimistic.findByOptimisticid", query = "SELECT e FROM Ebipessimistic e WHERE e.optimisticid = :optimisticid"),
    @NamedQuery(name = "Ebipessimistic.findByRecordid", query = "SELECT e FROM Ebipessimistic e WHERE e.recordid = :recordid"),
    @NamedQuery(name = "Ebipessimistic.findByModulename", query = "SELECT e FROM Ebipessimistic e WHERE e.modulename = :modulename"),
    @NamedQuery(name = "Ebipessimistic.findByUser", query = "SELECT e FROM Ebipessimistic e WHERE e.user = :user"),
    @NamedQuery(name = "Ebipessimistic.findByLockdate", query = "SELECT e FROM Ebipessimistic e WHERE e.lockdate = :lockdate"),
    @NamedQuery(name = "Ebipessimistic.findByStatus", query = "SELECT e FROM Ebipessimistic e WHERE e.status = :status")})
public class Ebipessimistic implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "OPTIMISTICID")
    private Integer optimisticid;
    @Column(name = "RECORDID")
    private Integer recordid;
    @Column(name = "MODULENAME")
    private String modulename;
    @Column(name = "USER")
    private String user;
    @Column(name = "LOCKDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lockdate;
    @Column(name = "STATUS")
    private Integer status;

    public Ebipessimistic() {}

    public Ebipessimistic(Integer optimisticid) {
        this.optimisticid = optimisticid;
    }

    public Integer getOptimisticid() {
        return optimisticid;
    }

    public void setOptimisticid(Integer optimisticid) {
        this.optimisticid = optimisticid;
    }

    public Integer getRecordid() {
        return recordid;
    }

    public void setRecordid(Integer recordid) {
        this.recordid = recordid;
    }

    public String getModulename() {
        return modulename;
    }

    public void setModulename(String modulename) {
        this.modulename = modulename;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getLockdate() {
        return lockdate;
    }

    public void setLockdate(Date lockdate) {
        this.lockdate = lockdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (optimisticid != null ? optimisticid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ebipessimistic)) {
            return false;
        }
        Ebipessimistic other = (Ebipessimistic) object;
        if ((this.optimisticid == null && other.optimisticid != null) || (this.optimisticid != null && !this.optimisticid.equals(other.optimisticid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Ebipessimistic[ optimisticid=" + optimisticid + " ]";
    }
    
}
