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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "COMPANYACTIVITIES", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companyactivities.findAll", query = "SELECT c FROM Companyactivities c"),
    @NamedQuery(name = "Companyactivities.findByActivityid", query = "SELECT c FROM Companyactivities c WHERE c.activityid = :activityid"),
    @NamedQuery(name = "Companyactivities.findByActivityname", query = "SELECT c FROM Companyactivities c WHERE c.activityname = :activityname"),
    @NamedQuery(name = "Companyactivities.findByActivitytype", query = "SELECT c FROM Companyactivities c WHERE c.activitytype = :activitytype"),
    @NamedQuery(name = "Companyactivities.findByActivitystatus", query = "SELECT c FROM Companyactivities c WHERE c.activitystatus = :activitystatus"),
    @NamedQuery(name = "Companyactivities.findByDuedate", query = "SELECT c FROM Companyactivities c WHERE c.duedate = :duedate"),
    @NamedQuery(name = "Companyactivities.findByDuration", query = "SELECT c FROM Companyactivities c WHERE c.duration = :duration"),
    @NamedQuery(name = "Companyactivities.findByAcolor", query = "SELECT c FROM Companyactivities c WHERE c.acolor = :acolor"),
    @NamedQuery(name = "Companyactivities.findByActivityclosedate", query = "SELECT c FROM Companyactivities c WHERE c.activityclosedate = :activityclosedate"),
    @NamedQuery(name = "Companyactivities.findByActivityisclosed", query = "SELECT c FROM Companyactivities c WHERE c.activityisclosed = :activityisclosed"),
    @NamedQuery(name = "Companyactivities.findByIslock", query = "SELECT c FROM Companyactivities c WHERE c.islock = :islock"),
    @NamedQuery(name = "Companyactivities.findByCreatedfrom", query = "SELECT c FROM Companyactivities c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companyactivities.findByCreateddate", query = "SELECT c FROM Companyactivities c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companyactivities.findByChangedfrom", query = "SELECT c FROM Companyactivities c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companyactivities.findByChangeddate", query = "SELECT c FROM Companyactivities c WHERE c.changeddate = :changeddate"),
    @NamedQuery(name = "Companyactivities.findByTimerstart", query = "SELECT c FROM Companyactivities c WHERE c.timerstart = :timerstart"),
    @NamedQuery(name = "Companyactivities.findByTimerdisabled", query = "SELECT c FROM Companyactivities c WHERE c.timerdisabled = :timerdisabled")})
public class Companyactivities implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACTIVITYID")
    private Integer activityid;
    @Column(name = "ACTIVITYNAME")
    private String activityname;
    @Column(name = "ACTIVITYTYPE")
    private String activitytype;
    @Column(name = "ACTIVITYSTATUS")
    private String activitystatus;
    @Column(name = "DUEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date duedate;
    @Column(name = "DURATION")
    private Integer duration;
    @Column(name = "ACOLOR")
    private String acolor;
    @Column(name = "ACTIVITYCLOSEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activityclosedate;
    @Column(name = "ACTIVITYISCLOSED")
    private Boolean activityisclosed;
    @Lob
    @Column(name = "ACTIVITYDESCRIPTION")
    private String activitydescription;
    @Column(name = "ISLOCK")
    private Boolean islock;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;
    @Column(name = "TIMERSTART")
    private Integer timerstart;
    @Column(name = "TIMERDISABLED")
    private Integer timerdisabled;
    @Lob
    @Column(name = "TIMER_EXTRA")
    private String timerExtra;
    @OneToMany(mappedBy = "companyactivities")
    private Set<Companyactivitiesdocs> companyactivitiesdocsSet  = new HashSet();
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;

    public Companyactivities() { }

    public Companyactivities(Integer activityid) {
        this.activityid = activityid;
    }

    public Integer getActivityid() {
        return activityid;
    }

    public void setActivityid(Integer activityid) {
        this.activityid = activityid;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    public String getActivitystatus() {
        return activitystatus;
    }

    public void setActivitystatus(String activitystatus) {
        this.activitystatus = activitystatus;
    }

    public Date getDuedate() {
        return duedate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getAcolor() {
        return acolor;
    }

    public void setAcolor(String acolor) {
        this.acolor = acolor;
    }

    public Date getActivityclosedate() {
        return activityclosedate;
    }

    public void setActivityclosedate(Date activityclosedate) {
        this.activityclosedate = activityclosedate;
    }

    public Boolean getActivityisclosed() {
        return activityisclosed;
    }

    public void setActivityisclosed(Boolean activityisclosed) {
        this.activityisclosed = activityisclosed;
    }

    public String getActivitydescription() {
        return activitydescription;
    }

    public void setActivitydescription(String activitydescription) {
        this.activitydescription = activitydescription;
    }

    public Boolean getIslock() {
        return islock;
    }

    public void setIslock(Boolean islock) {
        this.islock = islock;
    }

    public String getCreatedfrom() {
        return createdfrom;
    }

    public void setCreatedfrom(String createdfrom) {
        this.createdfrom = createdfrom;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
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

    public Integer getTimerstart() {
        return timerstart;
    }

    public void setTimerstart(Integer timerstart) {
        this.timerstart = timerstart;
    }

    public Integer getTimerdisabled() {
        return timerdisabled;
    }

    public void setTimerdisabled(Integer timerdisabled) {
        this.timerdisabled = timerdisabled;
    }

    public String getTimerExtra() {
        return timerExtra;
    }

    public void setTimerExtra(String timerExtra) {
        this.timerExtra = timerExtra;
    }

    @XmlTransient
    public Set<Companyactivitiesdocs> getCompanyactivitiesdocses() {
        return companyactivitiesdocsSet;
    }

    public void setCompanyactivitiesdocses(Set<Companyactivitiesdocs> companyactivitiesdocsSet) {
        this.companyactivitiesdocsSet = companyactivitiesdocsSet;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (activityid != null ? activityid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companyactivities)) {
            return false;
        }
        Companyactivities other = (Companyactivities) object;
        if ((this.activityid == null && other.activityid != null) || (this.activityid != null && !this.activityid.equals(other.activityid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companyactivities[ activityid=" + activityid + " ]";
    }
    
}
