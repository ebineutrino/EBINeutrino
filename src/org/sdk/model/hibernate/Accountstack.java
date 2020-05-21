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
@Table(name = "ACCOUNTSTACK", catalog = "EBINEUTRINODB", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Accountstack.findAll", query = "SELECT a FROM Accountstack a"),
    @NamedQuery(name = "Accountstack.findByAcstackid", query = "SELECT a FROM Accountstack a WHERE a.acstackid = :acstackid"),
    @NamedQuery(name = "Accountstack.findByAccountnr", query = "SELECT a FROM Accountstack a WHERE a.accountnr = :accountnr"),
    @NamedQuery(name = "Accountstack.findByAccountType", query = "SELECT a FROM Accountstack a WHERE a.accountType = :accountType"),
    @NamedQuery(name = "Accountstack.findByAccountDebit", query = "SELECT a FROM Accountstack a WHERE a.accountDebit = :accountDebit"),
    @NamedQuery(name = "Accountstack.findByAccountCredit", query = "SELECT a FROM Accountstack a WHERE a.accountCredit = :accountCredit"),
    @NamedQuery(name = "Accountstack.findByAccountDName", query = "SELECT a FROM Accountstack a WHERE a.accountDName = :accountDName"),
    @NamedQuery(name = "Accountstack.findByAccountCName", query = "SELECT a FROM Accountstack a WHERE a.accountCName = :accountCName"),
    @NamedQuery(name = "Accountstack.findByAccountCValue", query = "SELECT a FROM Accountstack a WHERE a.accountCValue = :accountCValue"),
    @NamedQuery(name = "Accountstack.findByAccountDValue", query = "SELECT a FROM Accountstack a WHERE a.accountDValue = :accountDValue"),
    @NamedQuery(name = "Accountstack.findByAccountname", query = "SELECT a FROM Accountstack a WHERE a.accountname = :accountname"),
    @NamedQuery(name = "Accountstack.findByAccountvalue", query = "SELECT a FROM Accountstack a WHERE a.accountvalue = :accountvalue"),
    @NamedQuery(name = "Accountstack.findByAccountdate", query = "SELECT a FROM Accountstack a WHERE a.accountdate = :accountdate"),
    @NamedQuery(name = "Accountstack.findByCreateddate", query = "SELECT a FROM Accountstack a WHERE a.createddate = :createddate"),
    @NamedQuery(name = "Accountstack.findByCreatedfrom", query = "SELECT a FROM Accountstack a WHERE a.createdfrom = :createdfrom"),
    @NamedQuery(name = "Accountstack.findByChangedfrom", query = "SELECT a FROM Accountstack a WHERE a.changedfrom = :changedfrom"),
    @NamedQuery(name = "Accountstack.findByChangeddate", query = "SELECT a FROM Accountstack a WHERE a.changeddate = :changeddate")})
public class Accountstack implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ACSTACKID")
    private Integer acstackid;
    @Column(name = "ACCOUNTNR")
    private String accountnr;
    @Lob
    @Column(name = "ACCOUNT")
    private String account;
    @Column(name = "ACCOUNT_TYPE")
    private Integer accountType;
    @Column(name = "ACCOUNT_DEBIT_TAX_TYPE")
    private String accountDebitTaxType;
    @Column(name = "ACCOUNT_CREDIT_TAX_TYPE")
    private String accountCreditTaxType;
    @Column(name = "TAX_VALUE")
    private Double accountTaxValue;
    @Column(name = "ACCOUNT_DEBIT")
    private String accountDebit;
    @Column(name = "ACCOUNT_CREDIT")
    private String accountCredit;
    @Column(name = "ACCOUNT_D_NAME")
    private String accountDName;
    @Column(name = "ACCOUNT_C_NAME")
    private String accountCName;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ACCOUNT_C_VALUE")
    private Double accountCValue;
    @Column(name = "ACCOUNT_D_VALUE")
    private Double accountDValue;
    @Column(name = "ACCOUNTNAME")
    private String accountname;
    @Column(name = "ACCOUNTVALUE")
    private Double accountvalue;
    @Lob
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "ACCOUNTDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date accountdate;
    @Column(name = "CREATEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createddate;
    @Column(name = "CREATEDFROM")
    private String createdfrom;
    @Column(name = "CHANGEDFROM")
    private String changedfrom;
    @Column(name = "CHANGEDDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changeddate;
    @OneToMany(mappedBy = "accountstack")
    private Set<Accountstackdocs> accountstackdocsSet = new HashSet();

    public Accountstack() { }

    public Accountstack(Integer acstackid) {
        this.acstackid = acstackid;
    }

    public Integer getAcstackid() {
        return acstackid;
    }

    public void setAcstackid(Integer acstackid) {
        this.acstackid = acstackid;
    }

    public String getAccountnr() {
        return accountnr;
    }

    public void setAccountnr(String accountnr) {
        this.accountnr = accountnr;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getAccountDebitTaxType() {
        return accountDebitTaxType;
    }

    public void setAccountDebitTaxType(String accountDebitTaxType) {
        this.accountDebitTaxType = accountDebitTaxType;
    }
    
    public String getAccountCreditTaxType() {
        return accountCreditTaxType;
    }

    public void setAccountCreditTaxType(String accountCreditTaxType) {
        this.accountCreditTaxType = accountCreditTaxType;
    }
    
    public Double getAccountTaxValue() {
        return accountTaxValue;
    }

    public void setAccountTaxValue(Double accountTaxValue) {
        this.accountTaxValue = accountTaxValue;
    }

    public String getAccountDebit() {
        return accountDebit;
    }

    public void setAccountDebit(String accountDebit) {
        this.accountDebit = accountDebit;
    }

    public String getAccountCredit() {
        return accountCredit;
    }

    public void setAccountCredit(String accountCredit) {
        this.accountCredit = accountCredit;
    }

    public String getAccountDName() {
        return accountDName;
    }

    public void setAccountDName(String accountDName) {
        this.accountDName = accountDName;
    }

    public String getAccountCName() {
        return accountCName;
    }

    public void setAccountCName(String accountCName) {
        this.accountCName = accountCName;
    }

    public Double getAccountCValue() {
        return accountCValue;
    }

    public void setAccountCValue(Double accountCValue) {
        this.accountCValue = accountCValue;
    }

    public Double getAccountDValue() {
        return accountDValue;
    }

    public void setAccountDValue(Double accountDValue) {
        this.accountDValue = accountDValue;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public Double getAccountvalue() {
        return accountvalue;
    }

    public void setAccountvalue(Double accountvalue) {
        this.accountvalue = accountvalue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAccountdate() {
        return accountdate;
    }

    public void setAccountdate(Date accountdate) {
        this.accountdate = accountdate;
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
    public Set<Accountstackdocs> getAccountstackdocses() {
        return accountstackdocsSet;
    }

    public void setAccountstackdocses(Set<Accountstackdocs> accountstackdocsSet) {
        this.accountstackdocsSet = accountstackdocsSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (acstackid != null ? acstackid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Accountstack)) {
            return false;
        }
        Accountstack other = (Accountstack) object;
        if ((this.acstackid == null && other.acstackid != null) || (this.acstackid != null && !this.acstackid.equals(other.acstackid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ebiNeutrinoSDK.model.hibernate.Accountstack[ acstackid=" + acstackid + " ]";
    }
    
}
