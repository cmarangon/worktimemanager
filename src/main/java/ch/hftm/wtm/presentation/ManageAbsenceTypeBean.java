
package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ch.hftm.wtm.business.ManageAbsenceTypeBusiness;
import ch.hftm.wtm.model.AbsenceType;

@ManagedBean(name = "manageAbsenceTypeBean")
@SessionScoped
public class ManageAbsenceTypeBean implements Serializable {

    public ManageAbsenceTypeBean() {
        System.out.println("manageAbsenceTypeBean=" + Thread.currentThread().getId());
    }

    public String onLoad() {
        System.out.println("Onload manageAbsenceTypeBean ausgeführt!");
        getAbsenceTypeByClientId();
        return null;
    }

    private static final long serialVersionUID = 8L;
    private List<AbsenceType> absenceTypeList;
    private ManageAbsenceTypeBusiness matb = new ManageAbsenceTypeBusiness();
    private AbsenceType absenceType = new AbsenceType();
    private boolean absenceTypeIsAlreadExistis = true;

    // Bean Incection
    @ManagedProperty(value = "#{loginBean}")
    private LoginBean loginBean;

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    // Hole die Absenztypen des entsprechenden Clienten
    public String getAbsenceTypeByClientId() {
        absenceTypeList = new ArrayList<AbsenceType>();
        absenceTypeList = matb.getAbsenceTypeByClientId(loginBean.getClientFromLoggedInPerson().getId());
        return null;
    }

    // Speichere den Absenztyp
    public String addAbsenceType() {

        absenceType.setClient(loginBean.getClientFromLoggedInPerson());
        matb.saveAbsenceType(absenceType);
        absenceType = new AbsenceType(); // erstelle neue Instanz der Absenz
        return null;
    }
    
    public String checkIfAbsenceTypeExisting(){
        
        absenceTypeIsAlreadExistis = matb.checkIfAbsencetypeExisting(absenceType, loginBean.getClientFromLoggedInPerson().getId());
        if(!absenceTypeIsAlreadExistis){
            
            
            addAbsenceType(); 
            
        }else{
            
            
            MessageFactory.error("ch.hftm.wtm.absence.ABSENCE_ALREADY_EXISTING");
            
        }
        
        
        return null;
    }

    public String deleteAbsenceType(AbsenceType absenceType) {
        matb.deleteAbsenceType(absenceType);
        return null;
    }

    public List<AbsenceType> getAbsenceTypeList() {
        return absenceTypeList;
    }

    public void setAbsenceTypeList(List<AbsenceType> absenceTypeList) {
        this.absenceTypeList = absenceTypeList;
    }

    public ManageAbsenceTypeBusiness getMatb() {
        return matb;
    }

    public void setMatb(ManageAbsenceTypeBusiness matb) {
        this.matb = matb;
    }

    public AbsenceType getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceType(AbsenceType absenceType) {
        this.absenceType = absenceType;
    }

    public String goToAbsenceTypeOverview() {
        // How can you do something stupid?
        getAbsenceTypeByClientId();
        return "overviewAbsenceType";
    }
}