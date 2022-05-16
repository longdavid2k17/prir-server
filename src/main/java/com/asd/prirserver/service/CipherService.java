package com.asd.prirserver.service;

import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

@Service
public class CipherService {

    private final String key = "Bar12345Bar12345"; // 128 bit key
    private final Key aesKey;
    private final Cipher cipher;
    public CipherService() throws NoSuchPaddingException, NoSuchAlgorithmException {
        aesKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher = Cipher.getInstance("AES");
    }

    public String encrypt(String txt) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(txt.getBytes());
        return new String(encrypted);
    }

    public String decrypt(String txt) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return new String(cipher.doFinal(txt.getBytes()));
    }
}
