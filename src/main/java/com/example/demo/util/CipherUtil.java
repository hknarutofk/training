package com.example.demo.util;

import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import lombok.extern.slf4j.Slf4j;
import sun.misc.HexDumpEncoder;

/**
 * 
 * @author yeqiang
 * @since 6/17/20 2:32 PM
 */
@Slf4j
public class CipherUtil {
    public static final HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
    static final String PEM_BEGIN_RSA_PRIVATE_KEY = "-----BEGIN RSA PRIVATE KEY-----\n";
    static final String PEM_END_RSA_PRIVATE_KEY = "-----END RSA PRIVATE KEY-----\n";
    static final String PEM_BEGIN_PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n";
    static final String PEM_END_PUBLIC_KEY = "-----END PUBLIC KEY-----\n";

    public static void saveKey(Key key, String savePath) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        fileOutputStream.write(key.getEncoded());
        fileOutputStream.close();
    }

    public static void savePublicKeyToPem(PublicKey publicKey, String savePath) throws IOException {
        String base64String = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        fileOutputStream.write(PEM_BEGIN_PUBLIC_KEY.getBytes());
        fileOutputStream.write(base64String.getBytes());
        fileOutputStream.write(PEM_END_PUBLIC_KEY.getBytes());
        fileOutputStream.close();
    }

    public static void saveRSAPrivateKeyToPem(RSAPrivateKey privateKey, String savePath) throws IOException {
        String base64String = Base64.getMimeEncoder().encodeToString(privateKey.getEncoded());
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        fileOutputStream.write(PEM_BEGIN_RSA_PRIVATE_KEY.getBytes());
        fileOutputStream.write(base64String.getBytes());
        fileOutputStream.write(PEM_END_RSA_PRIVATE_KEY.getBytes());
        fileOutputStream.close();
    }

    public static KeyPair generateKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    public static PublicKey loadPublicKey(byte[] keyBytes, String algorithm)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        log.debug(hexDumpEncoder.encode(keyBytes));
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        return publicKey;
    }

    public static PublicKey loadPublicKey(InputStream inputStream, String algorithm)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = new byte[inputStream.available()];
        inputStream.read(keyBytes);
        return loadPublicKey(keyBytes, algorithm);
    }

    public static PublicKey loadPublicKey(String filePath, String algorithm)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        return loadPublicKey(fileInputStream, algorithm);
    }

    public static PublicKey loadPublicKeyFromPem(String filePath, String algorithm)
        throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("-----")) {
                continue;
            }
            stringBuilder.append(line + "\n");
        }
        String base64String = stringBuilder.toString();
        log.debug(base64String);
        byte[] keyBytes = Base64.getMimeDecoder().decode(base64String);
        return loadPublicKey(keyBytes, algorithm);
    }

    public static PrivateKey loadPrivateKey(byte[] keyBytes, String algorithm)
        throws NoSuchAlgorithmException, InvalidKeySpecException {
        log.debug(hexDumpEncoder.encode(keyBytes));
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;

    }

    public static PrivateKey loadPrivateKey(String filePath, String algorithm)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        byte[] keyBytes = new byte[fileInputStream.available()];
        fileInputStream.read(keyBytes);
        fileInputStream.close();
        return loadPrivateKey(keyBytes, algorithm);
    }

    public static PrivateKey loadPrivateKeyFromPem(String filePath, String algorithm)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.contains("-----")) {
                continue;
            }
            stringBuilder.append(line + "\n");
        }
        String base64String = stringBuilder.toString();
        log.debug(base64String);
        byte[] keyBytes = Base64.getMimeDecoder().decode(base64String);
        return loadPrivateKey(keyBytes, algorithm);
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
