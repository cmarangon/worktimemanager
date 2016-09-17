package ch.hftm.wtm.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author HOEFI
 * @since 23.06.2016
 * @version 1.0
 */
@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "companyName", nullable = false, unique = true)
    private String companyName;

    @Column(name = "street", nullable = true)
    private String street;

    @Column(name = "postalCode", nullable = true)
    private String postalCode;

    @Column(name = "city", nullable = true)
    private String city;

    @Column(name = "country", nullable = true)
    private String country;

    @Column(name = "generalOfficePhone", nullable = true)
    private String generalOfficePhone;

    @Column(name = "isActive")
    private Boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "clientAdmin", nullable = true)
    private Person clientAdmin;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Person> employees;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<Region> clientRegions;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<MasterData> masterDatas;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<PublicHoliday> publicHolidays;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<AbsenceType> absenceType;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<FiscalPeriod> fiscalPeriods;

    public Client() {}

    public Client(String name) {
        companyName = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getGeneralOfficePhone() {
        return generalOfficePhone;
    }

    public void setGeneralOfficePhone(String generalOfficePhone) {
        this.generalOfficePhone = generalOfficePhone;
    }

    public Boolean isActive() {
        return isActive;
    }

    /**
     * JSF akzeptiert die "is" getter-Methode nur für boolean und nicht für
     * Boolean Datentypen.
     *
     * @return isActive
     */
    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Person getClientAdmin() {
        return clientAdmin;
    }

    public void setClientAdmin(Person clientAdmin) {
        this.clientAdmin = clientAdmin;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Person> employees) {
        this.employees = employees;
    }

    public List<MasterData> getMasterDatas() {
        return masterDatas;
    }

    public void setMasterDatas(List<MasterData> masterDatas) {
        this.masterDatas = masterDatas;
    }

    public List<PublicHoliday> getPublicHolidays() {
        return publicHolidays;
    }

    public void setPublicHolidays(List<PublicHoliday> publicHolidays) {
        this.publicHolidays = publicHolidays;
    }

    public List<AbsenceType> getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceType(List<AbsenceType> absenceType) {
        this.absenceType = absenceType;
    }

    public void setClientRegions(List<Region> clientRegions) {
        this.clientRegions = clientRegions;
    }

    public List<Region> getClientRegions() {
        return clientRegions;
    }

    public List<FiscalPeriod> getFiscalPeriods() {
        return fiscalPeriods;
    }

    public void setFiscalPeriods(List<FiscalPeriod> fiscalPeriods) {
        this.fiscalPeriods = fiscalPeriods;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Client c = (Client) obj;
        if (id.equals(c.id))
            return true;

        return false;
    }
}
