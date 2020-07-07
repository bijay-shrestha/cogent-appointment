package com.cogent.cogentappointment.client;

import com.cogent.cogentappointment.commons.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class CogentAppointmentClientApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void testToken() {

        Map<String, Object> map = new HashMap<>();
        map.put("transaction_code", "047Y07V");
        map.put("product_code", "CHEERS");
        map.put("esewa_id", "9860008245");
        String token = JwtUtils.generateToken(map);
        System.out.println(token);

    }


}
