package ch.hftm.wtm.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ch.hftm.wtm.enumeration.UserRole;

/**
 *
 * @author HOEFI
 * @since 06.06.2016
 * @version 1.0
 */
@Entity
@Table(name = "Person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique=true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "street", nullable = true)
    private String street;

    @Column(name = "postalCode", nullable = true)
    private String postalCode;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "country", nullable = true)
    private String country;

    @Column(name = "privatePhoneNumber", nullable = true)
    private String privatePhoneNumber;

    @Column(name = "businessPhoneNumber", nullable = true)
    private String businessPhoneNumber;

    @Column(name = "birthDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(name = "commencementDate", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date commencementDate;

    @Column(name = "leavingDate", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date leavingDate;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @Column(name = "isBlocked")
    private Boolean isBlocked = false;

    @Column(name = "badPwCounter", nullable = false)
    private Integer badPwCounter = new Integer(0);

    @Column(name = "passwordResetToken", nullable = true)
    private String passwordResetToken;

    @Column(name = "departmentName", nullable = true)
    private String departmentName;

    @Column(name = "assignedUserRole", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private UserRole assignedUserRole = UserRole.USER;

    @ManyToOne
    @JoinColumn(name = "supervisor", nullable = true)
    private Person supervisor;

    @OneToMany(mappedBy = "supervisor", fetch = FetchType.LAZY)
    private List<Person> employees;

    @ManyToOne
    @JoinColumn(name = "client", nullable = true)
    private Client client;

    @OneToMany(mappedBy = "approvedBy", fetch = FetchType.LAZY)
    private List<Stamping> approvedEntities;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<EmploymentPeriod> employmentPeriods;

    @ManyToMany
    @JoinTable(name = "person_region", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "region_id"))
    private Set<Region> employmentRegion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPrivatePhoneNumber() {
        return privatePhoneNumber;
    }

    public void setPrivatePhoneNumber(String privatePhoneNumber) {
        this.privatePhoneNumber = privatePhoneNumber;
    }

    public String getBusinessPhoneNumber() {
        return businessPhoneNumber;
    }

    public void setBusinessPhoneNumber(String businessPhoneNumber) {
        this.businessPhoneNumber = businessPhoneNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getCommencementDate() {
        return commencementDate;
    }

    public void setCommencementDate(Date commencementDate) {
        this.commencementDate = commencementDate;
    }

    public Date getLeavingDate() {
        return leavingDate;
    }

    public void setLeavingDate(Date leavingDate) {
        this.leavingDate = leavingDate;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Integer getBadPwCounter() {
        return badPwCounter;
    }

    public void setBadPwCounter(Integer badPwCounter) {
        this.badPwCounter = badPwCounter;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public UserRole getAssignedUserRole() {
        return assignedUserRole;
    }

    public void setAssignedUserRole(UserRole assignedUserRole) {
        this.assignedUserRole = assignedUserRole;
    }

    public Person getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Person supervisor) {
        this.supervisor = supervisor;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Person> employees) {
        this.employees = employees;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Stamping> getApprovedEntities() {
        return approvedEntities;
    }

    public void setApprovedEntities(List<Stamping> approvedEntities) {
        this.approvedEntities = approvedEntities;
    }

    public List<EmploymentPeriod> getEmploymentPeriods() {
        return employmentPeriods;
    }

    public void setEmploymentPeriods(List<EmploymentPeriod> employmentPeriods) {
        this.employmentPeriods = employmentPeriods;
    }

    public Set<Region> getEmploymentRegion() {
        return employmentRegion;
    }

    public void setEmploymentRegion(Set<Region> employmentRegion) {
        this.employmentRegion = employmentRegion;
    }

    public String toString()
    {
        return lastName + ", " + firstName + ": " + birthDate.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
