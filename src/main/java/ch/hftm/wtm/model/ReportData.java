package ch.hftm.wtm.model;

import java.util.List;

public class ReportData implements Comparable<ReportData>{
    private String workingday;
    private String absenceType;
    private List<String> stamping;
    private String worktime;
    
   
    public String getWorkingday() {
        return workingday;
    }

    public void setWorkingday(String workingday) {
        this.workingday = workingday;
    }

    public String getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceType(String absenceType) {
        this.absenceType = absenceType;
    }

    public List<String> getStamping() {
        return stamping;
    }

    public void setStamping(List<String> stamping) {
        this.stamping = stamping;
    }

    public String getWorktime() {
        return worktime;
    }

    public void setWorktime(String worktime) {
        this.worktime = worktime;
    }

    @Override
    public int compareTo(ReportData o) {
        return o.getWorkingday().compareTo(o.getWorkingday());
    }
}
