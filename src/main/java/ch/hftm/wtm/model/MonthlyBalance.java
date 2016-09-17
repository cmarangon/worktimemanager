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
 * @author HOEFI
 * @since 06.07.2016
 * @version 1.0
 */
@Entity
@Table(name = "MonthlyBalance")
public class MonthlyBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "timeFrame", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date timeFrame;

    @Column(name = "overtimeBalance", nullable = false)
    @Temporal(TemporalType.TIME)
    private Date overtimeBalance;

    @Column(name = "vacationDaysBalance", nullable = false)
    private Double vacationDaysBalance;

    @ManyToOne
    @JoinColumn(name = "assignedPerson", nullable = false)
    private Person assignedPerson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(Date timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Date getOvertimeBalance() {
        return overtimeBalance;
    }

    public void setOvertimeBalance(Date overtimeBalance) {
        this.overtimeBalance = overtimeBalance;
    }

    public Double getVacationDaysBalance() {
        return vacationDaysBalance;
    }

    public void setVacationDaysBalance(Double vacationDaysBalance) {
        this.vacationDaysBalance = vacationDaysBalance;
    }

    public Person getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(Person assignedPerson) {
        this.assignedPerson = assignedPerson;
    }
}
