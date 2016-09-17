package ch.hftm.wtm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
@Table(name = "AnnualBalanceSheet")
public class AnnualBalanceSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "totalOvertime", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date totalOvertime;

    @Column(name = "totalUntakenVacationDays", nullable = false)
    private Integer totalUntakenVacationDays;

    @OneToOne(mappedBy = "annualBalanceSheet", optional = false)
    private FiscalPeriod fiscalPeriod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTotalOvertime() {
        return totalOvertime;
    }

    public void setTotalOvertime(Date totalOvertime) {
        this.totalOvertime = totalOvertime;
    }

    public Integer getTotalUntakenVacationDays() {
        return totalUntakenVacationDays;
    }

    public void setTotalUntakenVacationDays(Integer totalUntakenVacationDays) {
        this.totalUntakenVacationDays = totalUntakenVacationDays;
    }

    public FiscalPeriod getFiscalPeriod() {
        return fiscalPeriod;
    }

    public void setFiscalPeriod(FiscalPeriod fiscalPeriod) {
        this.fiscalPeriod = fiscalPeriod;
    }

}
