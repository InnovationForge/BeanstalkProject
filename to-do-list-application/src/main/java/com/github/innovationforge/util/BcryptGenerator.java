package com.github.innovationforge.util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptGenerator {

    public static void main(String[] args) {
        String password = "niranjan";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        System.out.println("Original Password: " + password);
        System.out.println("Bcrypt Hash: " + hashedPassword);
    }
}
