package com.cogent.cogentappointment.admin.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Query;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

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
    public void test1() throws ParseException {

        SimpleDateFormat dateParser = new SimpleDateFormat("h:mm a");

        // Parse the time string
        Date date = dateParser.parse("3:30 AM");

        // Declare a date format for printing
        SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");

        // Print the previously parsed time
        System.out.println(dateFormater.format(date));
        String time24 = dateFormater.format(date);

        LocalDateTime date1 = LocalDateTime.of(                      // Represent a date with time-of-day, but lacking in zone/offset so NOT a moment, NOT a point on the timeline.
                LocalDate.parse("2018-01-23"),  // Parse the date-only value.
                LocalTime.parse("21:54")         // Parse the time-of-day.
        );
        System.out.println(date1.toLocalDate());

        LocalDate ld = LocalDate.parse("2018-01-23");

        LocalTime lt = LocalTime.parse("21:54");

        LocalDateTime ldt = LocalDateTime.of(ld, lt);
        System.out.println(ldt.toLocalDate());

        Date date12 = new Date();
        Timestamp ts=new Timestamp(date12.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(ts));



    }
}
