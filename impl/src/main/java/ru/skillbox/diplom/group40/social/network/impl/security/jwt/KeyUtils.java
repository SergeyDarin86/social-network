package ru.skillbox.diplom.group40.social.network.impl.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyUtils {

    @Value("${security.jwt.public-key-pem}")
    private String publicKeyPEM;

    @Value("${security.jwt.private-key-pem}")
    private String privateKeyPEM;

    public RSAPublicKey getAccessTokenPublicKey() {
        try {
            String publicKeyPEMContent = publicKeyPEM
                    .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPEMContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load public key", e);
        }
    }

    public RSAPrivateKey getAccessTokenPrivateKey() {
        try {
            String privateKeyPEMContent = privateKeyPEM
                    .replaceAll("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replaceAll("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEMContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load private key", e);
        }
    }
}
