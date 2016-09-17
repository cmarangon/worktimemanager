package ch.hftm.wtm.presentation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import ch.hftm.wtm.business.StampingBusiness;
import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.Stamping;
import ch.hftm.wtm.model.StampingList;
import ch.hftm.wtm.model.StampingList2;

@ManagedBean(name = "manageStampingBean")
@SessionScoped
public class ManageStampingBean implements Serializable {
    
    @ManagedProperty(value= "#{loginBean}")
    private LoginBean loginBean;
    

    public LoginBean getLoginBean() {
        return loginBean;
    }

    
    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
    

    private static final long serialVersionUID = 3312116334434039550L;
    
    private Date fromDate = new Date();
    private Date untilDate = new Date();
    private List<Stamping> stampingListAll; 
    private StampingList stampingList01 = new StampingList();
    private StampingList stampingList02 = new StampingList(); 
    private List<Stamping> stampingList1;  
    private List<Stamping> stampingList2; 
    private Stamping selectetStamping;
    private Stamping changedStamping;
    private Date stampingDateTime;
    private Date oldStampingDateTime;
    private String commentStamping;
    private List<Integer> integerList = new ArrayList<>();
    private List<StampingList> stampingList;
    
    //Hole selectierte Stempelung 
    public String selectRow(Stamping selectetStamping){
        System.out.println("Wurde geklickt ManageStampingBean");
        this.selectetStamping = selectetStamping;
        System.out.println("Selectierte Stempelung :" + selectetStamping);
        stampingDateTime = selectetStamping.getStampingTime();
        oldStampingDateTime = selectetStamping.getStampingTime();
        commentStamping = selectetStamping.getComment();
        return "manageStamping.xhtml";
    }
                    
    public Stamping stampingFromList2(Stamping index){
        Integer indexInt = stampingList1.indexOf(index);
        Stamping stampFromList2 = stampingList2.get(indexInt);
        System.out.println(stampFromList2.getStampingTime());
        return stampFromList2;
    }
    
    public Date getOldStampingDateTime() {
        return oldStampingDateTime;
    }


    
    public void setOldStampingDateTime(Date oldStampingDateTime) {
        this.oldStampingDateTime = oldStampingDateTime;
    }


    public Stamping getSelectetStamping() {
        return selectetStamping;
    }
    
    public void setSelectetStamping(Stamping selectetStamping) {
        this.selectetStamping = selectetStamping;
        this.stampingDateTime = selectetStamping.getStampingTime();
        this.commentStamping = selectetStamping.getComment();
    }
    
    public Date getStampingDateTime() {
        return stampingDateTime;
    }


    
    public void setStampingDateTime(Date stampingDateTime) {
        this.stampingDateTime = stampingDateTime;
    }

    

    
    public String getCommentStamping() {
        return commentStamping;
    }


    
    public void setCommentStamping(String commentStamping) {
        this.commentStamping = commentStamping;
    }

    
    public StampingList getStampingList01() {
        return stampingList01;
    }


    
    public void setStampingList01(StampingList stampingList01) {
        this.stampingList01 = stampingList01;
    }


    
    public StampingList getStampingList02() {
        return stampingList02;
    }


    
    public void setStampingList02(StampingList stampingList02) {
        this.stampingList02 = stampingList02;
    }


    public List<Stamping> getStampingListAll() {
        return stampingListAll;
    }


    
    public void setStampingListAll(List<Stamping> stampingList) {
        this.stampingListAll = stampingList;
    }
    
    public List<Stamping> getStampingList1() {
        return stampingList1;
    }
    
    public Stamping getStampingList1Index(Integer index) {
        
        
        return stampingList1.get(index);
    }


    
    public void setStampingList1(List<Stamping> stampingList) {
        this.stampingList1 = stampingList;
    }
    
    public List<Stamping> getStampingList2() {
        return stampingList2;
    }


    
    public void setStampingList2(List<Stamping> stampingList) {
        this.stampingList2 = stampingList;
    }


    public Date getFromDate() {
        return fromDate;
    }

    
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    
    public Date getUntilDate() {
        return untilDate;
    }

    
    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }
    
    //Hole alle Stempelungen von dem ausgewählten Zeitraum und fülle diese in die Liste
    public void getStamps() {
        StampingBusiness sb = new StampingBusiness();
        
        System.out.println("FromDate: " + fromDate);
        System.out.println("UntilDate: " + untilDate);
        
        //Hole aktive employmentPeriod
        Person loggetPerson = loginBean.getLoggedInPerson();
        List<EmploymentPeriod> employmentPeriodList = loggetPerson.getEmploymentPeriods();
        System.out.println("Liste geholt");
        System.out.println("Liste :" + employmentPeriodList);
        for (EmploymentPeriod ep: employmentPeriodList){
            System.out.println("Liste durchsuchen");
            //Überprüfe ob es die aktive Employment Period ist
            if(ep.isActive())
            {
                System.out.println("Eine aktive Employment Period gefunden");
                //Speichere die Zeit und übergebe den aktiven EmploymentPeriod
                
                //Zwischenspeichern in eine GesamtListe
                stampingListAll = sb.getStamps(fromDate, untilDate, ep);
                Stamping tempStemp;
                //StempelListe aufteilen
                stampingList1 = new ArrayList<>();
                stampingList2 = new ArrayList<>();
                List<Integer> tempIntList = new ArrayList<>();
                for(int i=0; stampingListAll.size()>i ;i++){
                    System.out.println("StampingListAll");
                    if(i%2 == 0)
                    {
                        System.out.println("Ungerade");
                        tempStemp = stampingListAll.get(i);
                        System.out.println("TempStemp : " + tempStemp);
                        System.out.println("StampingList1: " + stampingList1);
                        System.out.println("Hinzufügen : " + stampingList1.add(tempStemp));
                        System.out.println("StampingList1: " + stampingList1);
                    }
                    else
                    {
                        System.out.println("Gerade");
                        tempStemp = stampingListAll.get(i);
                        System.out.println("TempStemp : " + tempStemp);
                        stampingList2.add(tempStemp);
                    }
                }
                stampingList01.setStampingList1(stampingList1);
                StampingList2 stampingList23temp = new StampingList2();
                stampingList01.setStampingList2(stampingList23temp);
                stampingList = new ArrayList<>();
                stampingList.add(stampingList01);
//                stampingList01.setStampingList1(stampingList1);
//                stampingList01.setStampingList(stampingList1);
//                stampingList01.setStampingList(stampingList1);
//                stampingList01.setStampingList2(stampingList2);
//                stampingList02.setStampingList(stampingList2);
                setIntegerList(tempIntList);
        
        System.out.println("Ausgabe Liste im Bean");
        
        for (Stamping s: stampingListAll){

            System.out.println("****************************************");
            //ID der Stempelung 
            System.out.println("ID Stempelung :" +s.getId());
            
            //Zeitstempel 
            Date tempStamp = s.getStampingTime();
            System.out.println("Zeit :" + tempStamp);
            //Kommentar der Stempelung
            System.out.println("Kommentar :" + s.getComment());
            System.out.println("****************************************");
            
        }
    }
}
    }
    
    //Speichere die Änderung
    public void saveChangeStamping(){
        
        StampingBusiness sb = new StampingBusiness();
        
        System.out.println("Speichere die Änderung");
        changedStamping = selectetStamping;
        changedStamping.setStampingTime(getStampingDateTime());
        changedStamping.setComment(getCommentStamping());
        //Hole aktive Persom
        Person loggetPerson = loginBean.getLoggedInPerson();
        
        sb.setEditStamping(changedStamping, loggetPerson);
        
    }
    
    //Lösche die Stempelung
    public void deleteStamping(){
        System.out.println("Lösche die Stempelung");
    }
    
    //Abbrechen der Funktion
    public void cancelChangeStamping(){
        System.out.println("Abbrechen der Änderung");
    }
    
    //Hole alle Stempelungen
    public void getAllStamps() {
       StampingBusiness sb = new StampingBusiness();
       sb.getAllStamps();
       System.out.println("Hole alle Stempelungen");
        
    }


    public List<Integer> getIntegerList() {
        return integerList;
    }


    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }


    public List<StampingList> getStampingList() {
        return stampingList;
    }


    public void setStampingList(List<StampingList> stampingList) {
        this.stampingList = stampingList;
    }

}
