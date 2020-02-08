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

       String sql = "SELECT\n" +
               "CASE \n" +
               "WHEN \n" +
               "(((TIMESTAMPDIFF(YEAR, dateOfBirth, CURDATE()))<=0) AND\n" +
               "((TIMESTAMPDIFF(MONTH, dateOfBirth, CURDATE()) % 12)<=0))\n" +
               "THEN \n" +
               "CONCAT((FLOOR(TIMESTAMPDIFF(DAY, dateOfBirth, CURDATE()) % 30.4375)),'DAYS')\n" +
               "WHEN \n" +
               "((TIMESTAMPDIFF(YEAR, dateOfBirth ,CURDATE()))<=0)\n" +
               "THEN \n" +
               "CONCAT(((TIMESTAMPDIFF(MONTH, dateOfBirth, CURDATE()) % 12)), 'MONTHS')\n" +
               "ELSE \n" +
               "CONCAT(((TIMESTAMPDIFF(YEAR, dateOfBirth ,CURDATE()))), 'years')\n" +
               "END AS age\n" +
               "FROM \n" +
               "    Patient p \n";

        Query query = testEntityManager.getEntityManager().createQuery(sql);

        Object result = query.getResultList();


    }
}
