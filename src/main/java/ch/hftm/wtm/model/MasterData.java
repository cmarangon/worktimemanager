package ch.hftm.wtm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "MasterData")
public class MasterData {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive = true;

    @Column(name = "backwardEntryModification", nullable = true)
    private Integer backwardEntryModification;

    @Column(name = "defaultAnnualVacationDays", nullable = true)
    private Integer defaultAnnualVacationDays;

    @Column(name = "defaultDailyWorkingHours", nullable = true)
    private Double defaultDailyWorkingHours;

    @Column(name = "defaultOvertimeMaximum", nullable = true)
    private Integer defaultOvertimeMaximum;

    @Column(name = "endFiscalYear", nullable = true)
    private Integer endFiscalYear;

    @Column(name = "startFiscalYear", nullable = true)
    private Integer startFiscalYear;

    @Column(name = "validFromDate", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date validFromDate;

    @Column(name = "validToDate", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date validToDate;

    @ManyToOne
    @JoinColumn(name = "client", nullable = false)
    private Client client;
    
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

    public Integer getBackwardEntryModification() {
        return backwardEntryModification;
    }

    public void setBackwardEntryModification(Integer backwardEntryModification) {
        this.backwardEntryModification = backwardEntryModification;
    }

    public Integer getDefaultAnnualVacationDays() {
        return defaultAnnualVacationDays;
    }

    public void setDefaultAnnualVacationDays(Integer defaultAnnualVacationDays) {
        this.defaultAnnualVacationDays = defaultAnnualVacationDays;
    }

    public Double getDefaultDailyWorkingHours() {
        return defaultDailyWorkingHours;
    }

    public void setDefaultDailyWorkingHours(Double defaultDailyWorkingHours) {
        this.defaultDailyWorkingHours = defaultDailyWorkingHours;
    }

    public Integer getDefaultOvertimeMaximum() {
        return defaultOvertimeMaximum;
    }

    public void setDefaultOvertimeMaximum(Integer defaultOvertimeMaximum) {
        this.defaultOvertimeMaximum = defaultOvertimeMaximum;
    }

    public Integer getEndFiscalYear() {
        return endFiscalYear;
    }

    public void setEndFiscalYear(Integer endFiscalYear) {
        this.endFiscalYear = endFiscalYear;
    }

    public Integer getStartFiscalYear() {
        return startFiscalYear;
    }

    public void setStartFiscalYear(Integer startFiscalYear) {
        this.startFiscalYear = startFiscalYear;
    }

    public Date getValidFromDate() {
        return validFromDate;
    }

    public void setValidFromDate(Date validFromDate) {
        this.validFromDate = validFromDate;
    }

    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
