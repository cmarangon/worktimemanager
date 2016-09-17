package ch.hftm.wtm.business;

import java.util.Date;

/**
 * @author srdjankovacevic
 * @since 01.08.2016
 * @version 1.0
 */
public class CalculateAbsenceTime {

    private Date startDate;
    private Date endDate;

    private long diffSeconds = 0;
    private long diffMinutes = 0;
    private long diffHours = 0;
    private long diffDays = 0;

    /**
     * Konstruktor
     * 
     * @author srdjankovacevic
     * @since 01.08.2016
     * @param startDate
     * @param endDate
     */
    public CalculateAbsenceTime(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
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

    public long getDiffSeconds() {
        return diffSeconds;
    }

    public void setDiffSeconds(long diffSeconds) {
        this.diffSeconds = diffSeconds;
    }

    public long getDiffMinutes() {
        return diffMinutes;
    }

    public void setDiffMinutes(long diffMinutes) {
        this.diffMinutes = diffMinutes;
    }

    public long getDiffHours() {
        return diffHours;
    }

    public void setDiffHours(long diffHours) {
        this.diffHours = diffHours;
    }

    public long getDiffDays() {
        return diffDays;
    }

    public void setDiffDays(long diffDays) {
        this.diffDays = diffDays;
    }

    // Diese Methode berechnet die Zeit (Tage, Stunden, Minuten) zwischen dem Start- und dem Enddatum
    public CalculateAbsenceTime calculateTimeBetweenStartAndEndDate(Date startDate, Date endDate) {

        try {
            // in milliseconds
            long diff = endDate.getTime() - startDate.getTime();

            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CalculateAbsenceTime(startDate, endDate);
    }
}
