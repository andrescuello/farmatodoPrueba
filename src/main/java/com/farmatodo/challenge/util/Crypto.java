
package com.farmatodo.challenge.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class Crypto {
    private final SecretKey key;
    private final SecureRandom rng = new SecureRandom();

    public Crypto(@Value("${app.tokenization.aesKey:0123456789abcdef0123456789abcdef}") String key32) {
        if (key32.length() != 32) {
            throw new IllegalArgumentException("AES key must be 32 chars (256-bit)");
        }
        this.key = new SecretKeySpec(key32.getBytes(StandardCharsets.UTF_8), "AES");
    }

    public String encrypt(String plaintext) {
        try {
            byte[] iv = new byte[12];
            rng.nextBytes(iv);
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, iv));
            byte[] ct = c.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            ByteBuffer bb = ByteBuffer.allocate(iv.length + ct.length).put(iv).put(ct);
            return Base64.getEncoder().encodeToString(bb.array());
        } catch (Exception e) { throw new IllegalStateException(e); }
    }
}
