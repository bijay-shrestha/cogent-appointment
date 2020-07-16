package com.cogent.cogentappointment.admin.repository;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author smriti ON 18/01/2020
 */
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PatientRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

//    @Test
//    public void test() {
//        String query = " SELECT (TIMESTAMPDIFF(YEAR, p.dateOfBirth ,CURDATE()))" +
//                " AS age" +
//                " FROM Patient p WHERE p.id = 43";
//
//        Query query1 = testEntityManager.getEntityManager().createQuery(query);
//
//        Integer a = (Integer) query1.getSingleResult();
//
//        System.out.println(a);
//
//    }
}
