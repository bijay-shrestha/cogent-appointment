package com.cogent.cogentappointment.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Query;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author smriti ON 18/01/2020
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PatientRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void test() {
        String q = " SELECT" +
                " p.id as patientId," +                              //[0]
                " p.name as name," +                                //[2]
                " p.mobileNumber as mobileNumber," +                //[3]
                " p.address as address," +                          //[4]
                " p.email as email," +                              //[5]
                " p.gender as gender," +                            //[6]
                " p.dateOfBirth as dateOfBirth," +                  //[7]
                " p.nepaliDateOfBirth as nepaliDateOfBirth" +       //[8]
                " FROM Patient p" +
                " WHERE p.esewaId=:esewaId" +
                " AND p.isSelf=:isSelf" +
                " AND p.hospitalId.id=:hospitalId" +
                " AND p.status='Y'";

        Query query = testEntityManager.getEntityManager().createQuery(q)
                .setParameter("esewaId", "9849378680")
                .setParameter("hospitalId", 1L)
                .setParameter("isSelf", 'Y');

        Object result = query.getSingleResult();

        System.out.println(result);
    }

    @Test
    public void test1() {
        String time = "10 AM";

        String pattern = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(pattern);

        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString2 = dateFormat2.format(new Date()).toString();

        System.out.println(dateString2);


        String dateString4 = "21/12/2016 21.20";
        //old format
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy HH.mm");
        try{
            Date date4 = sdf3.parse(time);
            //new format
            SimpleDateFormat sdf4 = new SimpleDateFormat("dd/MM/yyyy hh.mm aa");
            //formatting the given time to new format with AM/PM
            System.out.println("Given date and time in AM/PM: "+sdf4.format(date4));
        }catch(ParseException e){
            e.printStackTrace();
        }

    }
}
