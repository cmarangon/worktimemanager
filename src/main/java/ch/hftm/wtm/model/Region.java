    package ch.hftm.wtm.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "Region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "name", nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "client", nullable = true)
    private Client client;

    @ManyToMany(mappedBy = "appliedRegions", fetch = FetchType.LAZY)
    private Set<PublicHoliday> publicHolidays;

    @ManyToMany(mappedBy = "employmentRegion", fetch = FetchType.LAZY)
    private Set<Person> appliedPublicHolidays;

    public Long getId() { 
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PublicHoliday> getPublicHolidays() {
        return publicHolidays;
    }

    public void setPublicHolidays(Set<PublicHoliday> publicHolidays) {
        this.publicHolidays = publicHolidays;
    }

    public Set<Person> getAppliedPublicHolidays() {
        return appliedPublicHolidays;
    }

    public void setAppliedPublicHolidays(Set<Person> appliedPublicHolidays) {
        this.appliedPublicHolidays = appliedPublicHolidays;
    }
    
    public void setClient(Client client)
    {
        this.client = client;
    }
    
    public Client getClient()
    {
        return client;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof Region && ((Region)object).id == this.id) {
            return true;
        } else {
            return false;
        }
    }
}
