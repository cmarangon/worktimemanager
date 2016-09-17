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
@Table(name = "Absence")
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "durationHours", nullable = true)
    @Temporal(TemporalType.TIME)
    private Date durationHours;

    @Column(name = "isVerified", nullable = true)
    private Boolean isVerified;

    @Column(name = "start", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date start;

    @Column(name = "end", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date end;

    @Column(name = "verifiedOn", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date verifiedOn;

    @ManyToOne
    @JoinColumn(name = "person", nullable = true)
    private EmploymentPeriod person;

    @ManyToOne
    @JoinColumn(name = "assignedAbsenceType", nullable = true)
    private AbsenceType assignedAbsenceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Date durationHours) {
        this.durationHours = durationHours;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getVerifiedOn() {
        return verifiedOn;
    }

    public void setVerifiedOn(Date verifiedOn) {
        this.verifiedOn = verifiedOn;
    }

    public EmploymentPeriod getPerson() {
        return person;
    }

    public void setPerson(EmploymentPeriod person) {
        this.person = person;
    }

    public AbsenceType getAssignedAbsenceType() {
        return assignedAbsenceType;
    }

    public void setAssignedAbsenceType(AbsenceType assignedAbsenceType) {
        this.assignedAbsenceType = assignedAbsenceType;
    }
}