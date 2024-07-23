package com.shivachi.pacs.demo.auth.utils;

import java.io.Serializable;
import java.util.Random;

public class PasswordGenerator implements Serializable {
    public static String generatePassword() {
        String characters = "01234ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz56789";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while (sb.length() < 8) {
            int index = (int) (rnd.nextFloat() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
