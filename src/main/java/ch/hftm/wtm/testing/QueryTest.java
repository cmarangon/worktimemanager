package ch.hftm.wtm.testing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.hftm.wtm.model.EmploymentPeriod;
import ch.hftm.wtm.model.Person;
import ch.hftm.wtm.model.Stamping;
import ch.hftm.wtm.persistence.StampingPersistence;
import ch.hftm.wtm.presentation.LoginBean;

public class QueryTest {

    public static void main(String[] args) {
        StampingPersistence sp = new StampingPersistence();
        List<Stamping> stl = new ArrayList<>();
        List<EmploymentPeriod> ep = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fromDate = new Date();
        Date tillDate = new Date();

        LoginBean lb = new LoginBean();
        Person person = lb.getLoggedInPerson();
        
        try {
            fromDate = sdf.parse("29/08/2016");
            tillDate = sdf.parse("26/09/2016");
        } catch (ParseException e) {
        }

        stl.addAll(sp.getAllStampingsByMonth(person, fromDate, tillDate));

        //stl.forEach(e-> System.out.println(e.getStampingTime()));
        
    }

}
