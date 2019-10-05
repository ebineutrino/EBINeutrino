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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "COMPANYMEETINGDOC", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companymeetingdoc.findAll", query = "SELECT c FROM Companymeetingdoc c"),
    @NamedQuery(name = "Companymeetingdoc.findByMeetingdocid", query = "SELECT c FROM Companymeetingdoc c WHERE c.meetingdocid = :meetingdocid"),
    @NamedQuery(name = "Companymeetingdoc.findByName", query = "SELECT c FROM Companymeetingdoc c WHERE c.name = :name"),
    @NamedQuery(name = "Companymeetingdoc.findByCreateddate", query = "SELECT c FROM Companymeetingdoc c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companymeetingdoc.findByCreatedfrom", query = "SELECT c FROM Companymeetingdoc c WHERE c.createdfrom = :createdfrom")})
public class Companymeetingdoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MEETINGDOCID")
    private Integer meetingdocid;
    @Column(name = "NAME")
    private String name;
    @Lob
    @Column(name = "FILES")
    private byte[] files;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @JoinColumn(name = "MEETINGID", referencedColumnName = "MEETINGPROTOCOLID")
    @ManyToOne
    private Companymeetingprotocol companymeetingprotocol;

    public Companymeetingdoc(){}

    public Companymeetingdoc(Integer meetingdocid) {
        this.meetingdocid = meetingdocid;
    }

    public Integer getMeetingdocid() {
        return meetingdocid;
    }

    public void setMeetingdocid(Integer meetingdocid) {
        this.meetingdocid = meetingdocid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFiles() {
        return files;
    }

    public void setFiles(byte[] files) {
        this.files = files;
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

    public Companymeetingprotocol getCompanymeetingprotocol() {
        return companymeetingprotocol;
    }

    public void setCompanymeetingprotocol(Companymeetingprotocol companymeetingprotocol) {
        this.companymeetingprotocol = companymeetingprotocol;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (meetingdocid != null ? meetingdocid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companymeetingdoc)) {
            return false;
        }
        Companymeetingdoc other = (Companymeetingdoc) object;
        if ((this.meetingdocid == null && other.meetingdocid != null) || (this.meetingdocid != null && !this.meetingdocid.equals(other.meetingdocid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companymeetingdoc[ meetingdocid=" + meetingdocid + " ]";
    }
    
}
