package com.intel.spring.boot.sample.boot.utils;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ecic Chen on 2016/1/28.
 */
public class JWTUtils {


    public static Map<String,Object> verifierToken(String token,String key){
        Base64 decoder = new Base64();
        //byte[] secret = "my secret".getBytes();
        byte[] secret = decoder.encodeBase64(key.getBytes());
        Map<String,Object> decodedPayload = null;
        try {
            decodedPayload = new JWTVerifier(secret).verify(token);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (JWTVerifyException e) {
            e.printStackTrace();
        }
        return decodedPayload;
    }


    public static String signerToken(Map<String,Object> claims,String key){
        Base64 decoder = new Base64();
        JWTSigner signer = new JWTSigner(decoder.encodeBase64(key.getBytes()));

        if(claims == null){
            claims = new HashMap<String, Object>();
        }

        return signer.sign(claims);
    }

}
