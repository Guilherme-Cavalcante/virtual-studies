package br.ifsp.virtual_studies.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

public class PemUtils {

    public static RSAPublicKey loadPublicKey(InputStream inputStream)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        String pem = readToString(inputStream);
        pem = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(pem);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static RSAPrivateKey loadPrivateKey(InputStream inputStream)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        String pem = readToString(inputStream);
        pem = pem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(pem);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    private static String readToString(InputStream inputStream) throws IOException {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    // @Bean
    // public static RSAPublicKey publicKey(@Value("classpath:certs/public.pem") Resource resource) throws Exception {
    //     return PemUtils.loadPublicKey(resource.getInputStream());
    // }

    // @Bean
    // public static RSAPrivateKey privateKey(@Value("classpath:certs/private.pem") Resource resource) throws Exception {
    //     return PemUtils.loadPrivateKey(resource.getInputStream());
    // }
}