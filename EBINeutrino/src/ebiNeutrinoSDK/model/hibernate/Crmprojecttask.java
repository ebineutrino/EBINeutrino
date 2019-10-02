package ebiNeutrinoSDK.model.hibernate;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "CRMPROJECTTASK", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Crmprojecttask.findAll", query = "SELECT c FROM Crmprojecttask c"),
    @NamedQuery(name = "Crmprojecttask.findByTaskiid", query = "SELECT c FROM Crmprojecttask c WHERE c.taskiid = :taskiid"),
    @NamedQuery(name = "Crmprojecttask.findByTaskid", query = "SELECT c FROM Crmprojecttask c WHERE c.taskid = :taskid"),
    @NamedQuery(name = "Crmprojecttask.findByX", query = "SELECT c FROM Crmprojecttask c WHERE c.x = :x"),
    @NamedQuery(name = "Crmprojecttask.findByY", query = "SELECT c FROM Crmprojecttask c WHERE c.y = :y"),
    @NamedQuery(name = "Crmprojecttask.findByName", query = "SELECT c FROM Crmprojecttask c WHERE c.name = :name"),
    @NamedQuery(name = "Crmprojecttask.findByStatus", query = "SELECT c FROM Crmprojecttask c WHERE c.status = :status"),
    @NamedQuery(name = "Crmprojecttask.findByType", query = "SELECT c FROM Crmprojecttask c WHERE c.type = :type"),
    @NamedQuery(name = "Crmprojecttask.findByDuration", query = "SELECT c FROM Crmprojecttask c WHERE c.duration = :duration"),
    @NamedQuery(name = "Crmprojecttask.findByColor", query = "SELECT c FROM Crmprojecttask c WHERE c.color = :color"),
    @NamedQuery(name = "Crmprojecttask.findByDone", query = "SELECT c FROM Crmprojecttask c WHERE c.done = :done")})
public class Crmprojecttask implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "TASKIID")
    private Integer taskiid;
    @Basic(optional = false)
    @Column(name = "TASKID")
    private String taskid;
    @Lob
    @Column(name = "PARENTSTASKID")
    private String parentstaskid;
    @Column(name = "X")
    private Integer x;
    @Column(name = "Y")
    private Integer y;
    @Column(name = "NAME")
    private String name;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "DURATION")
    private Integer duration;
    @Column(name = "COLOR")
    private String color;
    @Column(name = "DONE")
    private Integer done;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "crmprojecttask")
    private Set<Crmprojectcost> crmprojectcostSet = new HashSet();
    @JoinColumn(name = "PROJECTID", referencedColumnName = "PROJECTID")
    @ManyToOne
    private Crmproject crmproject;
    @OneToMany(mappedBy = "crmprojecttask")
    private Set<Crmprojectprop> crmprojectpropSet = new HashSet();

    public Crmprojecttask() {}

    public Crmprojecttask(Integer taskiid) {
        this.taskiid = taskiid;
    }

    public Crmprojecttask(Integer taskiid, String taskid) {
        this.taskiid = taskiid;
        this.taskid = taskid;
    }

    public Integer getTaskiid() {
        return taskiid;
    }

    public void setTaskiid(Integer taskiid) {
        this.taskiid = taskiid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getParentstaskid() {
        return parentstaskid;
    }

    public void setParentstaskid(String parentstaskid) {
        this.parentstaskid = parentstaskid;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Set<Crmprojectcost> getCrmprojectcosts() {
        return crmprojectcostSet;
    }

    public void setCrmprojectcosts(Set<Crmprojectcost> crmprojectcostSet) {
        this.crmprojectcostSet = crmprojectcostSet;
    }

    public Crmproject getCrmproject() {
        return crmproject;
    }

    public void setCrmproject(Crmproject crmproject) {
        this.crmproject = crmproject;
    }

    @XmlTransient
    public Set<Crmprojectprop> getCrmprojectprops() {
        return crmprojectpropSet;
    }

    public void setCrmprojectprops(Set<Crmprojectprop> crmprojectpropSet) {
        this.crmprojectpropSet = crmprojectpropSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskiid != null ? taskiid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Crmprojecttask)) {
            return false;
        }
        Crmprojecttask other = (Crmprojecttask) object;
        if ((this.taskiid == null && other.taskiid != null) || (this.taskiid != null && !this.taskiid.equals(other.taskiid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Crmprojecttask[ taskiid=" + taskiid + " ]";
    }
    
}
