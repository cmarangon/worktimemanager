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

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

/**
 *
 * @author HOEFI
 * @since 23.06.2016
 * @version 1.0
 */
@Entity
@Table(name = "Stamping")
@NamedQueries({
        @NamedQuery(name = Stamping.FIND_ALL_STAMPINGS_BY_DATE, query = "SELECT s FROM Stamping s WHERE stampingTime >= :startDate AND stampingTime <= :endDate AND assignedPerson = :period")
})
public class Stamping {
    public static final String FIND_ALL_STAMPINGS_BY_DATE = "Stamping.findStampingByDate";

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "stampingTime", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date stampingTime;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "verificationStatus", nullable = true)
    private String verificationStatus;

    @Column(name = "verifiedOn", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date verifiedOn;

    @ManyToOne
    @JoinColumn(name = "approvedBy", nullable = true)
    private Person approvedBy;

    @ManyToOne
    @JoinColumn(name = "assignedPerson", nullable = true)
    private EmploymentPeriod assignedPerson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStampingTime() {
        return stampingTime;
    }

    public void setStampingTime(Date stampingTime) {
        this.stampingTime = stampingTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Date getVerifiedOn() {
        return verifiedOn;
    }

    public void setVerifiedOn(Date verifiedOn) {
        this.verifiedOn = verifiedOn;
    }

    public Person getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Person approvedBy) {
        this.approvedBy = approvedBy;
    }

    public EmploymentPeriod getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(EmploymentPeriod assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

}
