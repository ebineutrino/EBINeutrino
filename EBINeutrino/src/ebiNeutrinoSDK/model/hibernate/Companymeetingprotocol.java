package ebiNeutrinoSDK.model.hibernate;

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
@Table(name = "COMPANYMEETINGPROTOCOL", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companymeetingprotocol.findAll", query = "SELECT c FROM Companymeetingprotocol c"),
    @NamedQuery(name = "Companymeetingprotocol.findByMeetingprotocolid", query = "SELECT c FROM Companymeetingprotocol c WHERE c.meetingprotocolid = :meetingprotocolid"),
    @NamedQuery(name = "Companymeetingprotocol.findByMeetingsubject", query = "SELECT c FROM Companymeetingprotocol c WHERE c.meetingsubject = :meetingsubject"),
    @NamedQuery(name = "Companymeetingprotocol.findByMeetingtype", query = "SELECT c FROM Companymeetingprotocol c WHERE c.meetingtype = :meetingtype"),
    @NamedQuery(name = "Companymeetingprotocol.findByMetingdate", query = "SELECT c FROM Companymeetingprotocol c WHERE c.metingdate = :metingdate"),
    @NamedQuery(name = "Companymeetingprotocol.findByCreatedfrom", query = "SELECT c FROM Companymeetingprotocol c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companymeetingprotocol.findByCreateddate", query = "SELECT c FROM Companymeetingprotocol c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companymeetingprotocol.findByChangedfrom", query = "SELECT c FROM Companymeetingprotocol c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companymeetingprotocol.findByChangeddate", query = "SELECT c FROM Companymeetingprotocol c WHERE c.changeddate = :changeddate")})
public class Companymeetingprotocol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MEETINGPROTOCOLID")
    private Integer meetingprotocolid;
    @Column(name = "MEETINGSUBJECT")
    private String meetingsubject;
    @Column(name = "MEETINGTYPE")
    private String meetingtype;
    @Lob
    @Column(name = "PROTOCOL")
    private String protocol;
    @Column(name = "METINGDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date metingdate;
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
    @OneToMany(mappedBy = "companymeetingprotocol")
    private Set<Companymeetingcontacts> companymeetingcontactsSet  = new HashSet();
    @OneToMany(mappedBy = "companymeetingprotocol")
    private Set<Companymeetingdoc> companymeetingdocSet  = new HashSet();
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;

    public Companymeetingprotocol() { }

    public Companymeetingprotocol(Integer meetingprotocolid) {
        this.meetingprotocolid = meetingprotocolid;
    }

    public Integer getMeetingprotocolid() {
        return meetingprotocolid;
    }

    public void setMeetingprotocolid(Integer meetingprotocolid) {
        this.meetingprotocolid = meetingprotocolid;
    }

    public String getMeetingsubject() {
        return meetingsubject;
    }

    public void setMeetingsubject(String meetingsubject) {
        this.meetingsubject = meetingsubject;
    }

    public String getMeetingtype() {
        return meetingtype;
    }

    public void setMeetingtype(String meetingtype) {
        this.meetingtype = meetingtype;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Date getMetingdate() {
        return metingdate;
    }

    public void setMetingdate(Date metingdate) {
        this.metingdate = metingdate;
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

    @XmlTransient
    public Set<Companymeetingcontacts> getCompanymeetingcontactses() {
        return companymeetingcontactsSet;
    }

    public void setCompanymeetingcontactsSet(Set<Companymeetingcontacts> companymeetingcontactsSet) {
        this.companymeetingcontactsSet = companymeetingcontactsSet;
    }

    @XmlTransient
    public Set<Companymeetingdoc> getCompanymeetingdocs() {
        return companymeetingdocSet;
    }

    public void setCompanymeetingdocs(Set<Companymeetingdoc> companymeetingdocSet) {
        this.companymeetingdocSet = companymeetingdocSet;
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
        hash += (meetingprotocolid != null ? meetingprotocolid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companymeetingprotocol)) {
            return false;
        }
        Companymeetingprotocol other = (Companymeetingprotocol) object;
        if ((this.meetingprotocolid == null && other.meetingprotocolid != null) || (this.meetingprotocolid != null && !this.meetingprotocolid.equals(other.meetingprotocolid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companymeetingprotocol[ meetingprotocolid=" + meetingprotocolid + " ]";
    }
    
}
