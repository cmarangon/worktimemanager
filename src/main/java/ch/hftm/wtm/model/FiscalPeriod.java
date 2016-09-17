
package ch.hftm.wtm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author HOEFI
 * @since 06.07.2016
 * @version 1.0
 */
@Entity
@Table(name = "FiscalPeriod")
public class FiscalPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "startDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "completedOn", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date completedOn;

    @ManyToOne
    @JoinColumn(name = "completedBy", nullable = true)
    private Person completedBy;

    @OneToOne
    @JoinColumn(name = "annualBalanceSheet", nullable = true)
    private AnnualBalanceSheet annualBalanceSheet;

    @ManyToOne
    @JoinColumn(name = "client", nullable = false)
    private Client client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(Date completedOn) {
        this.completedOn = completedOn;
    }

    public Person getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(Person completedBy) {
        this.completedBy = completedBy;
    }

    public AnnualBalanceSheet getAnnualBalanceSheet() {
        return annualBalanceSheet;
    }

    public void setAnnualBalanceSheet(AnnualBalanceSheet annualBalanceSheet) {
        this.annualBalanceSheet = annualBalanceSheet;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    @Override
    public boolean equals(Object object) {
        if(object instanceof FiscalPeriod && ((FiscalPeriod)object).id.equals(this.id)) {
            return true;
        } else {
            return false;
        }
    }
}
