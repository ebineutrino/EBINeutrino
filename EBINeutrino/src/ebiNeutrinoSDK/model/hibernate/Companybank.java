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
@Table(name = "COMPANYBANK", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Companybank.findAll", query = "SELECT c FROM Companybank c"),
    @NamedQuery(name = "Companybank.findByBankid", query = "SELECT c FROM Companybank c WHERE c.bankid = :bankid"),
    @NamedQuery(name = "Companybank.findByBankname", query = "SELECT c FROM Companybank c WHERE c.bankname = :bankname"),
    @NamedQuery(name = "Companybank.findByBankbsb", query = "SELECT c FROM Companybank c WHERE c.bankbsb = :bankbsb"),
    @NamedQuery(name = "Companybank.findByBankaccount", query = "SELECT c FROM Companybank c WHERE c.bankaccount = :bankaccount"),
    @NamedQuery(name = "Companybank.findByBankbic", query = "SELECT c FROM Companybank c WHERE c.bankbic = :bankbic"),
    @NamedQuery(name = "Companybank.findByBankiban", query = "SELECT c FROM Companybank c WHERE c.bankiban = :bankiban"),
    @NamedQuery(name = "Companybank.findByBankcountry", query = "SELECT c FROM Companybank c WHERE c.bankcountry = :bankcountry"),
    @NamedQuery(name = "Companybank.findByCreatedfrom", query = "SELECT c FROM Companybank c WHERE c.createdfrom = :createdfrom"),
    @NamedQuery(name = "Companybank.findByCreateddate", query = "SELECT c FROM Companybank c WHERE c.createddate = :createddate"),
    @NamedQuery(name = "Companybank.findByChangedfrom", query = "SELECT c FROM Companybank c WHERE c.changedfrom = :changedfrom"),
    @NamedQuery(name = "Companybank.findByChangeddate", query = "SELECT c FROM Companybank c WHERE c.changeddate = :changeddate")})
public class Companybank implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BANKID")
    private Integer bankid;
    @Column(name = "BANKNAME")
    private String bankname;
    @Column(name = "BANKBSB")
    private String bankbsb;
    @Column(name = "BANKACCOUNT")
    private String bankaccount;
    @Column(name = "BANKBIC")
    private String bankbic;
    @Column(name = "BANKIBAN")
    private String bankiban;
    @Column(name = "BANKCOUNTRY")
    private String bankcountry;
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
    @JoinColumn(name = "COMPANYID", referencedColumnName = "COMPANYID")
    @ManyToOne
    private Company company;

    public Companybank() {}

    public Companybank(Integer bankid) {
        this.bankid = bankid;
    }

    public Integer getBankid() {
        return bankid;
    }

    public void setBankid(Integer bankid) {
        this.bankid = bankid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankbsb() {
        return bankbsb;
    }

    public void setBankbsb(String bankbsb) {
        this.bankbsb = bankbsb;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getBankbic() {
        return bankbic;
    }

    public void setBankbic(String bankbic) {
        this.bankbic = bankbic;
    }

    public String getBankiban() {
        return bankiban;
    }

    public void setBankiban(String bankiban) {
        this.bankiban = bankiban;
    }

    public String getBankcountry() {
        return bankcountry;
    }

    public void setBankcountry(String bankcountry) {
        this.bankcountry = bankcountry;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bankid != null ? bankid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Companybank)) {
            return false;
        }
        Companybank other = (Companybank) object;
        if ((this.bankid == null && other.bankid != null) || (this.bankid != null && !this.bankid.equals(other.bankid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Companybank[ bankid=" + bankid + " ]";
    }
    
}
