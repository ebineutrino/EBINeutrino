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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "SET_REPORTPARAMETER", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SetReportparameter.findAll", query = "SELECT s FROM SetReportparameter s"),
    @NamedQuery(name = "SetReportparameter.findByParamid", query = "SELECT s FROM SetReportparameter s WHERE s.paramid = :paramid"),
    @NamedQuery(name = "SetReportparameter.findByPosition", query = "SELECT s FROM SetReportparameter s WHERE s.position = :position"),
    @NamedQuery(name = "SetReportparameter.findByCreateddate", query = "SELECT s FROM SetReportparameter s WHERE s.createddate = :createddate"),
    @NamedQuery(name = "SetReportparameter.findByCreatedfrom", query = "SELECT s FROM SetReportparameter s WHERE s.createdfrom = :createdfrom"),
    @NamedQuery(name = "SetReportparameter.findByParamname", query = "SELECT s FROM SetReportparameter s WHERE s.paramname = :paramname"),
    @NamedQuery(name = "SetReportparameter.findByParamtype", query = "SELECT s FROM SetReportparameter s WHERE s.paramtype = :paramtype")})
public class SetReportparameter implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PARAMID")
    private Integer paramid;
    @Column(name = "POSITION")
    private Integer position;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "PARAMNAME")
    private String paramname;
    @Column(name = "PARAMTYPE")
    private String paramtype;
    @JoinColumn(name = "REPORTID", referencedColumnName = "IDREPORTFORMODULE")
    @ManyToOne
    private SetReportformodule setReportformodule;

    public SetReportparameter() {}

    public SetReportparameter(Integer paramid) {
        this.paramid = paramid;
    }

    public Integer getParamid() {
        return paramid;
    }

    public void setParamid(Integer paramid) {
        this.paramid = paramid;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public String getParamname() {
        return paramname;
    }

    public void setParamname(String paramname) {
        this.paramname = paramname;
    }

    public String getParamtype() {
        return paramtype;
    }

    public void setParamtype(String paramtype) {
        this.paramtype = paramtype;
    }

    public SetReportformodule getSetReportformodule() {
        return setReportformodule;
    }

    public void setSetReportformodule(SetReportformodule setReportformodule) {
        this.setReportformodule = setReportformodule;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paramid != null ? paramid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SetReportparameter)) {
            return false;
        }
        SetReportparameter other = (SetReportparameter) object;
        if ((this.paramid == null && other.paramid != null) || (this.paramid != null && !this.paramid.equals(other.paramid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.SetReportparameter[ paramid=" + paramid + " ]";
    }
    
}
