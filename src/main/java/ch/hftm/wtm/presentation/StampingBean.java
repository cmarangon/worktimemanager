package ch.hftm.wtm.presentation;

import java.io.Serializable;

import java.text.SimpleDateFormat;
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

@ManagedBean(name = "stampingBean")
@SessionScoped
public class StampingBean implements Serializable {

    @ManagedProperty(value = "#{loginBean}")
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

    // private Person id;
   //private List<Stamping> stampingList;

//    private Person id;
    private List<Stamping> stampingListAll; 
    private StampingList stampingList; 
//    private List<ArrayList<?>> stampingList;
    private List<Stamping> stampingList1;  
    private List<Stamping> stampingList2; 

    public StampingList getStampingList() {


//    public StampingList getStampingList() {

        return stampingList;
    }

    
    public void setStampingList(StampingList stampingList) {
        this.stampingList = stampingList;
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


    
    public void setStampingList1(List<Stamping> stampingList) {
        this.stampingList1 = stampingList;
    }
    
    public List<Stamping> getStampingList2() {
        return stampingList2;
    }
    public Stamping getStampingList2(Integer index) {
        return stampingList2.get(index);
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

    // Neue Stempelung mit aktueller Zeit erfassen
    public String setStamp() {

        // Neue Stempelung Objekt erstellen
        StampingBusiness stamp = new StampingBusiness();

        System.out.println("Gestempelt");
        Person loggetPerson = loginBean.getLoggedInPerson();
        System.out.println("Vorname :" + loggetPerson.getFirstName());

        // Hole aktive employmentPeriod
        List<EmploymentPeriod> employmentPeriodList = loggetPerson.getEmploymentPeriods();
        System.out.println("Liste geholt");
        System.out.println("Liste :" + employmentPeriodList);
        for (EmploymentPeriod ep : employmentPeriodList) {
            System.out.println("Liste durchsuchen");
            // Überprüfe ob es die aktive Employment Period ist
            if (ep.isActive()) {
                System.out.println("Eine aktive Employment Period gefunden");
                // Speichere die Zeit und übergebe den aktiven EmploymentPeriod
                stamp.stamp(ep, loggetPerson);
            }
        }
        return null;
    }

    // Check ob User eingestempelt ist oder nicht
    // Wird benötigt, damit der Zustand des Stempel Button ermittelt werden kann.
    public boolean isUserStamptIn() {
        StampingBusiness stamp = new StampingBusiness();
        return stamp.isUserStamptIn(loginBean.getLoggedInPerson());
    }

    public String getLastStampingTime() {
        StampingBusiness sb = new StampingBusiness();
        SimpleDateFormat hhmm = new SimpleDateFormat("HH:mm");

        return hhmm.format(sb.getLastStamp(loginBean.getLoggedInPerson()).getStampingTime());
    }

    // Hole alle Stempelungen von dem ausgewählten Zeitraum und fülle diese in die Liste
    public void getStamps() {
        StampingBusiness sb = new StampingBusiness();
        // List<Stamping> stampList;

        System.out.println("FromDate: " + fromDate);
        System.out.println("UntilDate: " + untilDate);

        Person loggetPerson = loginBean.getLoggedInPerson();
        List<EmploymentPeriod> employmentPeriodList = loggetPerson.getEmploymentPeriods();
        System.out.println("Liste geholt");
        System.out.println("Liste :" + employmentPeriodList);
        for (EmploymentPeriod ep : employmentPeriodList) {
            System.out.println("Liste durchsuchen");
            // Überprüfe ob es die aktive Employment Period ist
            if (ep.isActive()) {
                System.out.println("Eine aktive Employment Period gefunden");

                //Speichere die Zeit und übergebe den aktiven EmploymentPeriod
                
                //Zwischenspeichern in eine GesamtListe
                stampingListAll = sb.getStamps(fromDate, untilDate, ep);
                Stamping tempStemp;
                List<Stamping> stampingList1 = new ArrayList<>();
                List<Stamping> stampingList2 = new ArrayList<>();
                //StempelListe aufteilen
                for(int i=0; stampingListAll.size()>i ;i++){
                    System.out.println("StampingListALl");
                    if(i%2 == 0)
                    {
                        System.out.println("Ungerade");
                        tempStemp = stampingListAll.get(i);
                        System.out.println("TempStemp : " + tempStemp);
                        System.out.println("StampingList1: " + stampingList1);
                        stampingList1.add(tempStemp);
//                        System.out.println("Hinzufügen : " + stampingList1.add(tempStemp));
                        System.out.println("StampingList1: " + stampingList1);
                        
                    }
                    else
                    {
                        System.out.println("Gerade");
                        tempStemp = stampingListAll.get(i);
                        System.out.println("TempStemp : " + tempStemp);
                        System.out.println("StampingList2: " + stampingList2);
                        stampingList2.add(tempStemp);
//                      System.out.println("Hinzufügen : " + stampingList1.add(tempStemp));
                      System.out.println("StampingList1: " + stampingList2);
//                        stampingList2.add(tempStemp);
                    }
                }
//                List<ArrayList<?>> stampingList = new ArrayList<ArrayList<?>>();
                System.out.println("Vor zusammenführung Objekt StampingListe");
                System.out.println("StampingListe1: " + this.stampingList1);
                System.out.println("StampingListe2: " + this.stampingList2);
                this.stampingList1 = stampingList1;
                this.stampingList2 = stampingList2;
//                stampingList.setStampingList1(stampingList1);
//                stampingList.setStampingList2(stampingList2);
//                this.stampingList1.addAll(stampingList1);
//                this.stampingList2.addAll(stampingList2);
//                stampingList.add((ArrayList<?>) stampingList1);
//                stampingList.add((ArrayList<?>) stampingList2);
//                stampingList.setStampingList1(stampingList1);
//                stampingList.setStampingList2(stampingList2);
                System.out.println("Nach zusammenführung Objekt StampingListe");
                System.out.println("StampingListe1: " + this.stampingList1);
                System.out.println("StampingListe2: " + this.stampingList2);
/*                for(int i=0; stampingListAll.size()>=i ;i++)
                {
                    if(i%2 == 0)
                    {
                        System.out.println("Ungerade");
                        Stamping tempStemp = stampingListAll.get(i);
                        System.out.println("TempStemp : " + tempStemp);
                        stampingList1.add(tempStemp);
//                        stampingList1.add(stampingListAll.get(i));
                        
                        
                    }
                    else
                    {
                        System.out.println("Gerade");
                        stampingList2.add(stampingListAll.get(i));
                    }
                }
                stampingList.setStampingList1(stampingList1);
                stampingList.setStampingList2(stampingList2);
                
                System.out.println("StampingListe1 " + stampingList.getStampingList1() );
                System.out.println("StampingListe2 " + stampingList.getStampingList2() );
*///                stampingList.add(stampingList1);
//                stampingList.a
                
    //        }
    //    }
        
        
//        List<Stamping> stampList = sb.getStamps(fromDate, untilDate, id);
        
        System.out.println("Ausgabe Liste im Bean");
        
        for (Stamping s: stampingListAll){


                // List<Stamping> stampList = sb.getStamps(fromDate, untilDate, id);

                System.out.println("Ausgabe Liste im Bean");


                    System.out.println("****************************************");
                    // ID der Stempelung
                    System.out.println("ID Stempelung :" + s.getId());

                    // Zeitstempel
                    Date tempStamp = s.getStampingTime();
                    System.out.println("Zeit :" + tempStamp);
                    /*
                     * System.out.println("Zeit: " + tempStamp.get(Calendar.HOUR_OF_DAY) + ":"
                     * + tempStamp.get(Calendar.MINUTE) + ":"
                     * + tempStamp.get(Calendar.SECOND)
                     * + "\nDatum: " + tempStamp.get(Calendar.DAY_OF_MONTH) + "."
                     * + (tempStamp.get(Calendar.MONTH) + 1) +"."
                     * + tempStamp.get(Calendar.YEAR));
                     */
                    // Kommentar der Stempelung
                    System.out.println("Kommentar :" + s.getComment());
                    System.out.println("****************************************");

                }
            }
        }
    }

    public boolean userCanStamp() {
        Person person = loginBean.getLoggedInPerson();
        if (person != null)
            for (EmploymentPeriod ep : person.getEmploymentPeriods())
                if (ep.isActive())
                    if (ep.getValidFrom().before(new Date())
                            && (ep.getValidTo() == null || ep.getValidTo().after(new Date())))
                        return true;

        return false;
    }

    // Hole alle Stempelungen
    public void getAllStamps() {
        StampingBusiness sb = new StampingBusiness();
        sb.getAllStamps();
        System.out.println("Hole alle Stempelungen");

    }


}
