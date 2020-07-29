package com.cogent.cogentappointment.admin.validator;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @author smriti on 2019-08-30
 */
public class LoginValidator {

    public static Boolean checkPassword(String enteredPassword, String originalPassword) {
        return BCrypt.checkpw(enteredPassword, originalPassword);
    }

}
