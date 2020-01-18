package com.cogent.cogentappointment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.Query;
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
                " p.title as title," +                              //[1]
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
}
