package com.example.destopinion.config;

import java.security.SecureRandom;
import java.util.Base64;

public class SaltGenerator {
    private  static  final int SALT_LENGTH = 16;

    public static String getSalt(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[SALT_LENGTH];
        random.nextBytes(bytes);
        String salt = Base64.getEncoder().encodeToString(bytes);
        return salt;
    }
}
