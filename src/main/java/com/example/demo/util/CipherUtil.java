package com.example.demo.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import lombok.extern.slf4j.Slf4j;
import sun.misc.HexDumpEncoder;

/**
 * @author yeqiang
 * @since 6/17/20 2:32 PM
 */
@Slf4j
public class CipherUtil {

    static final HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();

    public static KeyPair generateKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    public static PublicKey loadPublicKey(String filePath, String algorithm)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] keyBytes = new byte[fileInputStream.available()];
        fileInputStream.read(keyBytes);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        return publicKey;
    }

    public static PrivateKey loadPrivateKey(String filePath, String algorithm)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] keyBytes = new byte[fileInputStream.available()];
        fileInputStream.read(keyBytes);
        log.debug(hexDumpEncoder.encode(keyBytes));
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;
    }

    public static void saveKey(Key key, String savePath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        fileOutputStream.write(key.getEncoded());
        fileOutputStream.close();
    }

    public static boolean verify(PublicKey publicKey, byte[] data, String algorithm, byte[] signData)
        throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initVerify(publicKey);
        signature.update(data);
        boolean b = signature.verify(signData);
        return b;
    }

    public static byte[] sign(PrivateKey privateKey, byte[] data, String algorithm)
        throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance(algorithm);
        signature.initSign(privateKey);
        signature.update(data);
        byte[] result = signature.sign();
        return result;
    }

    public static void main(String[] args) throws Exception {
        String src = "test";
        KeyPair keyPair = generateKeyPair("RSA", 1024);
        // asn.1 PKCS#8 der编码
        log.debug(keyPair.getPrivate().getAlgorithm());
        log.debug(keyPair.getPrivate().getFormat());
        log.debug(hexDumpEncoder.encode(keyPair.getPrivate().getEncoded()));
        saveKey(keyPair.getPrivate(), "rsaPrivate.key");
        PrivateKey privateKey2 = loadPrivateKey("rsaPrivate.key", "RSA");
        log.debug(hexDumpEncoder.encode(privateKey2.getEncoded()));

        log.debug(hexDumpEncoder.encode(keyPair.getPublic().getEncoded()));
        saveKey(keyPair.getPublic(), "rsaPublic.key");
        PublicKey publicKey2 = loadPublicKey("rsaPublic.key", "RSA");
        log.debug(hexDumpEncoder.encode(publicKey2.getEncoded()));

        /*
        RSA
         */
        String algorithm = "MD5withRSA";
        byte[] signature = sign(keyPair.getPrivate(), src.getBytes(), algorithm);
        log.debug(hexDumpEncoder.encode(signature));
        boolean b = verify(keyPair.getPublic(), src.getBytes(), algorithm, signature);
        log.debug("{}", b);

        /*
        DSA
         */
        algorithm = "SHA1withDSA";
        keyPair = generateKeyPair("DSA", 1024);
        signature = sign(keyPair.getPrivate(), src.getBytes(), algorithm);
        log.debug(hexDumpEncoder.encode(signature));
        b = verify(keyPair.getPublic(), src.getBytes(), algorithm, signature);
        log.debug("{}", b);

        /*
        ECDSA
         */
        algorithm = "SHA1withECDSA";
        keyPair = generateKeyPair("EC", 256);
        signature = sign(keyPair.getPrivate(), src.getBytes(), algorithm);
        log.debug(hexDumpEncoder.encode(signature));
        b = verify(keyPair.getPublic(), src.getBytes(), algorithm, signature);
        log.debug("{}", b);

    }
}
