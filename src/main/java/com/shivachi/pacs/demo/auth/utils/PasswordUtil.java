package com.shivachi.pacs.demo.auth.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Slf4j
@Component
public class PasswordUtil implements Serializable, PasswordEncoder {
    @Value("${jwt.password.encoder.secret}")
    private String passwordSecret;

    @Value("${jwt.password.encoder.iteration}")
    private Integer iteration;

    @Value("${jwt.password.encoder.keylength}")
    private Integer keyLength;
    @Override
    public String encode(CharSequence cs) {
        try {
            byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
                    .generateSecret(new PBEKeySpec(cs.toString().toCharArray(), passwordSecret.getBytes(), iteration, keyLength))
                    .getEncoded();

            log.info(String.format("Generated Password By Custom Password Encoder [ %s ]",  Base64.getEncoder().encodeToString(result)));

            return Base64.getEncoder().encodeToString(result);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
