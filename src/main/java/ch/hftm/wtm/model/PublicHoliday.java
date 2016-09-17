package ch.hftm.wtm.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author srdjankovacevic
 * @since 27.06.2016
 * @version 1.0
 */

@Entity
@Table(name = "PublicHoliday")
public class PublicHoliday {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "dayOfTheYear", nullable = false)
    private Integer dayOfTheYear;

    @Column(name = "duration", nullable = false)
    private Double duration;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive = true;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "client", nullable = true)
    private Client client;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
               name = "publicHoliday_region", 
               joinColumns = @JoinColumn(name = "publicHoliday_id", referencedColumnName="id"), 
               inverseJoinColumns = @JoinColumn(name = "region_id", referencedColumnName="id"))
    private Set<Region> appliedRegions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDayOfTheYear() {
        return dayOfTheYear;
    }

    public void setDayOfTheYear(Integer dayOfTheYear) {
        this.dayOfTheYear = dayOfTheYear;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Region> getAppliedRegions() {
        return appliedRegions;
    }

    public void setAppliedRegions(Set<Region> appliedRegions) {
        this.appliedRegions = appliedRegions;
    }

}
