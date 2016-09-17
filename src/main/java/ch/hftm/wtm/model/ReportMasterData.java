package ch.hftm.wtm.model;

import java.util.Date;

public class ReportMasterData {
    private Date fromDate;
    private Date tillDate;
    private Person person;
    private Integer levelOfEmployment;
    private double actualTime;
    private double toTime;
    private double overtime;
    private double holidayBalanceOld;
    private double holidayBalanceNew;
    
    public ReportMasterData() {
    
    };
        
    public Date getFromDate() {
        return fromDate;
    }
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }
    public Date getTillDate() {
        return tillDate;
    }
    public void setTillDate(Date tillDate) {
        this.tillDate = tillDate;
    }
    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }
    public Integer getLevelOfEmployment() {
        return levelOfEmployment;
    }
    public void setLevelOfEmployment(Integer levelOfEmployment) {
        this.levelOfEmployment = levelOfEmployment;
    }
    public double getActualTime() {
        return actualTime;
    }
    public void setActualTime(double actualTime) {
        this.actualTime = actualTime;
    }
    public double getToTime() {
        return toTime;
    }
    public void setToTime(double toTime) {
        this.toTime = toTime;
    }
    public double getOvertime() {
        return overtime;
    }
    public void setOvertime(double overtime) {
        this.overtime = overtime;
    }
    public double getHolidayBalanceOld() {
        return holidayBalanceOld;
    }
    public void setHolidayBalanceOld(double holidayBalanceOld) {
        this.holidayBalanceOld = holidayBalanceOld;
    }
    public double getHolidayBalanceNew() {
        return holidayBalanceNew;
    }
    public void setHolidayBalanceNew(double holidayBalanceNew) {
        this.holidayBalanceNew = holidayBalanceNew;
    }
}
