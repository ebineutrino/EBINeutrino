package ebiNeutrinoSDK.model.hibernate;

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
@Table(name = "CRMCALENDAR", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmcalendar.findAll", query = "SELECT c FROM Crmcalendar c"),
    @NamedQuery(name = "Crmcalendar.findByCalendarid", query = "SELECT c FROM Crmcalendar c WHERE c.calendarid = :calendarid"),
    @NamedQuery(name = "Crmcalendar.findByCuser", query = "SELECT c FROM Crmcalendar c WHERE c.cuser = :cuser"),
    @NamedQuery(name = "Crmcalendar.findByName", query = "SELECT c FROM Crmcalendar c WHERE c.name = :name"),
    @NamedQuery(name = "Crmcalendar.findByColor", query = "SELECT c FROM Crmcalendar c WHERE c.color = :color"),
    @NamedQuery(name = "Crmcalendar.findByStartdate", query = "SELECT c FROM Crmcalendar c WHERE c.startdate = :startdate"),
    @NamedQuery(name = "Crmcalendar.findByEnddate", query = "SELECT c FROM Crmcalendar c WHERE c.enddate = :enddate")})
public class Crmcalendar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "CALENDARID")
    private Integer calendarid;
    @Column(name = "CUSER")
    private String cuser;
    @Column(name = "NAME")
    private String name;
    @Column(name = "COLOR")
    private String color;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "STARTDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startdate;
    @Column(name = "ENDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date enddate;
    @Lob
    @Column(name = "CICON")
    private byte[] cicon;

    public Crmcalendar() {}

    public Crmcalendar(Integer calendarid) {
        this.calendarid = calendarid;
    }

    public Integer getCalendarid() {
        return calendarid;
    }

    public void setCalendarid(Integer calendarid) {
        this.calendarid = calendarid;
    }

    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public byte[] getCicon() {
        return cicon;
    }

    public void setCicon(byte[] cicon) {
        this.cicon = cicon;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calendarid != null ? calendarid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmcalendar)) {
            return false;
        }
        Crmcalendar other = (Crmcalendar) object;
        if ((this.calendarid == null && other.calendarid != null) || (this.calendarid != null && !this.calendarid.equals(other.calendarid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmcalendar[ calendarid=" + calendarid + " ]";
    }
    
}
