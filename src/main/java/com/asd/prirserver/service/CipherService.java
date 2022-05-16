package com.asd.prirserver.service;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class CipherService {

    public String encrypt(String txt){
        return Base64.getEncoder().encodeToString(txt.getBytes());
    }

    public String decrypt(String txt){
        byte[] decodedBytes = Base64.getDecoder().decode(txt);
        return new String(decodedBytes);
    }
}
