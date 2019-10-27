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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "SET_REPORTFORMODULE", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SetReportformodule.findAll", query = "SELECT s FROM SetReportformodule s"),
    @NamedQuery(name = "SetReportformodule.findByIdreportformodule", query = "SELECT s FROM SetReportformodule s WHERE s.idreportformodule = :idreportformodule"),
    @NamedQuery(name = "SetReportformodule.findByReportname", query = "SELECT s FROM SetReportformodule s WHERE s.reportname = :reportname"),
    @NamedQuery(name = "SetReportformodule.findByReportcategory", query = "SELECT s FROM SetReportformodule s WHERE s.reportcategory = :reportcategory"),
    @NamedQuery(name = "SetReportformodule.findByReportdate", query = "SELECT s FROM SetReportformodule s WHERE s.reportdate = :reportdate"),
    @NamedQuery(name = "SetReportformodule.findByShowaspdf", query = "SELECT s FROM SetReportformodule s WHERE s.showaspdf = :showaspdf"),
    @NamedQuery(name = "SetReportformodule.findByShowaswindow", query = "SELECT s FROM SetReportformodule s WHERE s.showaswindow = :showaswindow"),
    @NamedQuery(name = "SetReportformodule.findByPrintauto", query = "SELECT s FROM SetReportformodule s WHERE s.printauto = :printauto"),
    @NamedQuery(name = "SetReportformodule.findByIsactive", query = "SELECT s FROM SetReportformodule s WHERE s.isactive = :isactive")})
public class SetReportformodule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDREPORTFORMODULE")
    private Integer idreportformodule;
    @Column(name = "REPORTNAME")
    private String reportname;
    @Column(name = "REPORTCATEGORY")
    private String reportcategory;
    @Lob
    @Column(name = "REPORTFILENAME")
    private String reportfilename;
    @Column(name = "REPORTDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportdate;
    @Column(name = "SHOWASPDF")
    private Boolean showaspdf;
    @Column(name = "SHOWASWINDOW")
    private Boolean showaswindow;
    @Column(name = "PRINTAUTO")
    private Boolean printauto;
    @Column(name = "ISACTIVE")
    private Boolean isactive;
    @OneToMany(mappedBy = "setReportformodule")
    private Set<SetReportparameter> setReportparameterSet = new HashSet();

    public SetReportformodule() {}

    public SetReportformodule(Integer idreportformodule) {
        this.idreportformodule = idreportformodule;
    }

    public Integer getIdreportformodule() {
        return idreportformodule;
    }

    public void setIdreportformodule(Integer idreportformodule) {
        this.idreportformodule = idreportformodule;
    }

    public String getReportname() {
        return reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getReportcategory() {
        return reportcategory;
    }

    public void setReportcategory(String reportcategory) {
        this.reportcategory = reportcategory;
    }

    public String getReportfilename() {
        return reportfilename;
    }

    public void setReportfilename(String reportfilename) {
        this.reportfilename = reportfilename;
    }

    public Date getReportdate() {
        return reportdate;
    }

    public void setReportdate(Date reportdate) {
        this.reportdate = reportdate;
    }

    public Boolean getShowaspdf() {
        return showaspdf;
    }

    public void setShowaspdf(Boolean showaspdf) {
        this.showaspdf = showaspdf;
    }

    public Boolean getShowaswindow() {
        return showaswindow;
    }

    public void setShowaswindow(Boolean showaswindow) {
        this.showaswindow = showaswindow;
    }

    public Boolean getPrintauto() {
        return printauto;
    }

    public void setPrintauto(Boolean printauto) {
        this.printauto = printauto;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    @XmlTransient
    public Set<SetReportparameter> getSetReportparameters() {
        return setReportparameterSet;
    }

    public void setSetReportparameters(Set<SetReportparameter> setReportparameterSet) {
        this.setReportparameterSet = setReportparameterSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idreportformodule != null ? idreportformodule.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SetReportformodule)) {
            return false;
        }
        SetReportformodule other = (SetReportformodule) object;
        if ((this.idreportformodule == null && other.idreportformodule != null) || (this.idreportformodule != null && !this.idreportformodule.equals(other.idreportformodule))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.SetReportformodule[ idreportformodule=" + idreportformodule + " ]";
    }
    
}
