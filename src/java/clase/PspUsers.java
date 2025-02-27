/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clase;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author eetxa
 */
@Entity
@Table(name = "psp_users")
@NamedQueries({
    @NamedQuery(name = "PspUsers.findAll", query = "SELECT p FROM PspUsers p"),
    @NamedQuery(name = "PspUsers.findByName", query = "SELECT p FROM PspUsers p WHERE p.name = :name"),
    @NamedQuery(name = "PspUsers.findByEmail", query = "SELECT p FROM PspUsers p WHERE p.email = :email"),
    @NamedQuery(name = "PspUsers.findByUsername", query = "SELECT p FROM PspUsers p WHERE p.username = :username"),
    @NamedQuery(name = "PspUsers.findByPassword", query = "SELECT p FROM PspUsers p WHERE p.password = :password"),
    @NamedQuery(name = "PspUsers.findByIsActive", query = "SELECT p FROM PspUsers p WHERE p.isActive = :isActive"),
    @NamedQuery(name = "PspUsers.findByValidationToken", query = "SELECT p FROM PspUsers p WHERE p.validationToken = :validationToken"),
    @NamedQuery(name = "PspUsers.findByActivationExpiryDate", query = "SELECT p FROM PspUsers p WHERE p.activationExpiryDate = :activationExpiryDate")})
public class PspUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Id
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "isActive")
    private boolean isActive;
    @Column(name = "validationToken")
    private BigInteger validationToken;
    @Column(name = "activationExpiryDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activationExpiryDate;

    public PspUsers() {
    }

    public PspUsers(String username) {
        this.username = username;
    }

    public PspUsers(String username, String name, String email, String password, boolean isActive, BigInteger validationToken, Date activationExpiryDate) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.validationToken=validationToken;
        this.activationExpiryDate=activationExpiryDate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public BigInteger getValidationToken() {
        return validationToken;
    }

    public void setValidationToken(BigInteger validationToken) {
        this.validationToken = validationToken;
    }

    public Date getActivationExpiryDate() {
        return activationExpiryDate;
    }

    public void setActivationExpiryDate(Date activationExpiryDate) {
        this.activationExpiryDate = activationExpiryDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PspUsers)) {
            return false;
        }
        PspUsers other = (PspUsers) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "clase.PspUsers[ username=" + username + " ]";
    }
    
}
