package ch.hftm.wtm.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author srdjankovacevic
 * @since 27.06.2016
 * @version 1.0
 */

@Entity
@Table(name = "EmploymentPeriod")
public class EmploymentPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive = true;

    @Column(name = "dailyWorkingHours", nullable = true)
    @Temporal(TemporalType.TIME)
    private Date dailyWorkingHours;

    @Column(name = "employmentLevelPercent", nullable = true)
    private Integer employmentLevelPercent = 100;

    @Column(name = "maxOvertime", nullable = true)
    @Temporal(TemporalType.TIME)
    private Date maxOvertime;

    @Column(name = "validFrom", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date validFrom;

    @Column(name = "validTo", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date validTo;

    @Column(name = "yearlyVacationDays", nullable = true)
    private Double yearlyVacationDays;

    @ManyToOne
    @JoinColumn(name = "employee", nullable = true)
    private Person employee;

    @OneToMany(mappedBy = "assignedPerson", fetch = FetchType.LAZY)
    private List<Stamping> timestamps;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<Absence> absences;

    @OneToMany(mappedBy = "assignedPerson", fetch = FetchType.LAZY)
    private List<MonthlyBalance> monthlyBalances;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getDailyWorkingHours() {
        return dailyWorkingHours;
    }

    public void setDailyWorkingHours(Date dailyWorkingHours) {
        this.dailyWorkingHours = dailyWorkingHours;
    }

    public Integer getEmploymentLevelPercent() {
        return employmentLevelPercent;
    }

    public void setEmploymentLevelPercent(Integer employmentLevelPercent) {
        this.employmentLevelPercent = employmentLevelPercent;
    }

    public Date getMaxOvertime() {
        return maxOvertime;
    }

    public void setMaxOvertime(Date maxOvertime) {
        this.maxOvertime = maxOvertime;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Double getYearlyVacationDays() {
        return yearlyVacationDays;
    }

    public void setYearlyVacationDays(Double yearlyVacationDays) {
        this.yearlyVacationDays = yearlyVacationDays;
    }

    public Person getEmployee() {
        return employee;
    }

    public void setEmployee(Person employee) {
        this.employee = employee;
    }

    public List<Stamping> getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(List<Stamping> timestamps) {
        this.timestamps = timestamps;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    public List<MonthlyBalance> getMonthlyBalances() {
        return monthlyBalances;
    }

    public void setMonthlyBalances(List<MonthlyBalance> monthlyBalances) {
        this.monthlyBalances = monthlyBalances;
    }
    
    @Override
    public boolean equals(Object object) {
        if(object instanceof EmploymentPeriod && ((EmploymentPeriod)object).id == this.id) {
            return true;
        } else {
            return false;
        }
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Ist aktiv: ");
        
        if(isActive)
            sb.append("Ja, ");
        else
            sb.append("Nein, ");
        
        if(validFrom != null)
            sb.append(validFrom.toString() + ": ");
        if(validTo != null)
            sb.append(validTo.toString());
        
        return sb.toString();
    }
}
