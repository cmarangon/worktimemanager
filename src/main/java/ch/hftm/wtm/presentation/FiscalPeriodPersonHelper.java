package ch.hftm.wtm.presentation;

import ch.hftm.wtm.model.Person;

/**
 *
 * @author HOEFI
 * @since 29.08.2016
 * @version 1.0 
 */
public class FiscalPeriodPersonHelper {
    
    private Person person = null;
    private double workedHours = 0;
    private double workingHours = 0;
    private boolean hasValidStampings = false;
    
    public Person getPerson() {
        return person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }
    
    public double getWorkedHours() {
        return workedHours;
    }
    
    public void setWorkedHours(double workedHours) {
        this.workedHours = workedHours;
    }
    
    public double getWorkingHours() {
        return workingHours;
    }
    
    public void setWorkingHours(double workingHours) {
        this.workingHours = workingHours;
    }
    
    public boolean isHasValidStampings() {
        return hasValidStampings;
    }

    
    public void setHasValidStampings(boolean hasValidStampings) {
        this.hasValidStampings = hasValidStampings;
    }

}
