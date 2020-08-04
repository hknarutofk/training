package com.example.demo.util;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author yeqiang
 * @since 6/17/20 4:53 PM
 */
@Slf4j
public class CipherUtilTest {

    /**
     * 在工程目录下运行sh bash_script/openssl_rsa.sh 生成key.pk8.der, pubkey.x509.der
     * 
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws InvalidKeySpecException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    @Test
    public void testOpenssl()
        throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        PrivateKey privateKey = CipherUtil.loadPrivateKey("key.pk8.der", "RSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(privateKey.getEncoded()));
        PublicKey publicKey = CipherUtil.loadPublicKey("pubkey.x509.der", "RSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(publicKey.getEncoded()));
        byte[] data = "1cfdde231dbc13b3bfdbc0c6430da839".getBytes();
        log.debug(CipherUtil.hexDumpEncoder.encode(data));
        byte[] signature = CipherUtil.sign(privateKey, data, "MD5withRSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(signature));
        boolean b = CipherUtil.verify(publicKey, data, "MD5withRSA", signature);
        log.debug("{}", b);
        Assert.assertTrue(b);
    }

    @Test
    public void testPemFormat()
        throws NoSuchAlgorithmException, IOException, InvalidKeySpecException, SignatureException, InvalidKeyException {
        PrivateKey privateKey = CipherUtil.loadPrivateKeyFromPem("key.pk8.pem", "RSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(privateKey.getEncoded()));
        PublicKey publicKey = CipherUtil.loadPublicKeyFromPem("pubkey.x509.pem", "RSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(publicKey.getEncoded()));
        byte[] data = "1cfdde231dbc13b3bfdbc0c6430da839".getBytes();
        log.debug(CipherUtil.hexDumpEncoder.encode(data));
        byte[] signature = CipherUtil.sign(privateKey, data, "MD5withRSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(signature));
        boolean b = CipherUtil.verify(publicKey, data, "MD5withRSA", signature);
        log.debug("{}", b);
        Assert.assertTrue(b);
    }

    @Test
    public void testPem() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        BufferedReader br = new BufferedReader(new FileReader("key.pk8.pem"));
        String s = br.readLine();
        String str = "";
        s = br.readLine();
        while (s.charAt(0) != '-') {
            str += s + "\n";
            s = br.readLine();
        }
        log.debug(str);
        BASE64Decoder base64decoder = new BASE64Decoder();
        byte[] b = base64decoder.decodeBuffer(str);

        // 生成私匙
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(b);
        PrivateKey privateKey = kf.generatePrivate(keySpec);
        log.debug("{}", privateKey);
    }

    @Test
    public void testBase64() {
        byte[] buffer = new byte[1024];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte)i;
        }

        String base64String1;
        base64String1 = Base64.getEncoder().encodeToString(buffer);
        log.debug(base64String1);
        base64String1 = Base64.getMimeEncoder().encodeToString(buffer);
        log.debug(base64String1);
        base64String1 = Base64.getUrlEncoder().encodeToString(buffer);
        System.out.println(base64String1);
        log.debug("=========================");
        base64String1 = new BASE64Encoder().encode(buffer);
        log.debug(base64String1);
    }

    /**
     * 与C交叉签名认证 https://github.com/hknarutofk/openssl-trainning/blob/master/cross_sign_verify_with_java.c
     * 
     * @throws Exception
     */
    @Test
    public void testCrossSignVerify() throws Exception {
        // 1. C openssl api 签名 -> Java 程序验证签名
        byte[] data = "1cfdde231dbc13b3bfdbc0c6430da839".getBytes();
        log.debug(CipherUtil.hexDumpEncoder.encode(data));
        byte[] signature;
        boolean b;

        FileInputStream fileInputStream = new FileInputStream("/tmp/c_sign.bin");
        signature = new byte[fileInputStream.available()];
        fileInputStream.read(signature);
        log.debug(CipherUtil.hexDumpEncoder.encode(signature));
        PublicKey publicKey = CipherUtil.loadPublicKeyFromPem("pubkey.x509.pem", "RSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(publicKey.getEncoded()));

        b = CipherUtil.verify(publicKey, data, "MD5withRSA", signature);
        log.debug("{}", b);
        Assert.assertTrue(b);

        // 2. Java签名 -> C程序验证签名
        PrivateKey privateKey = CipherUtil.loadPrivateKeyFromPem("key.pk8.pem", "RSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(privateKey.getEncoded()));

        signature = CipherUtil.sign(privateKey, data, "MD5withRSA");
        log.debug(CipherUtil.hexDumpEncoder.encode(signature));
        FileOutputStream fileOutputStream = new FileOutputStream("/tmp/java_sign.bin");
        fileOutputStream.write(signature);
        fileOutputStream.close();

        // 不采用内部摘要方案，做到与C接口相似
        byte[] md5Buffer = CipherUtil.md5(data);
        log.debug(CipherUtil.hexDumpEncoder.encode(md5Buffer));
        signature = CipherUtil.signNONEwithRSA(privateKey, md5Buffer);
        log.debug(CipherUtil.hexDumpEncoder.encode(signature));
    }

    @Test
    public void testloadPrivateKeyFromPemString() throws Exception {
        FileInputStream inputStream = new FileInputStream("key.pk8.pem");
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        inputStream.close();

        String text = new String(buffer);
        log.debug(text);

        PrivateKey privateKey = CipherUtil.loadPrivateKeyFromPemString(text, "RSA");
    }

    @Test
    public void test2() throws Exception {
        String in =
            "zC/GfP6rqbcWUkE4nlqgDeFAK4jLy5P2N4ec5i040ADosft3twjt1lKhhhVLKI+eW3JUBGF20J8ZBP5D6LoUw7qjBAApD0K/Q5OEjBxna3n6pAGqe9kNP45JscMiHIW81UElkyF2p1fnhb1MVc8g/9dIYKQJRBtFd+OFcz2+2CbmLbI5C4cBHtU+W7bCmAwGVJg4RF1SmVvMxjHrVjqwiRjkPJETGw5RRCd9e/lmoh65yMJwL6LubYcxawhaMRwylEwaa9R8kcz8u7q2wDAirISBt1q8be3CDmd5NzOXbF9MlKb69Kzl20esqr+mD8NR6/NIjgvMLANECosxTmMqR2pElHHzYMYxqO72g7Brdbjd38TWIgF5GJ4GFahGWU8QriLasBakU1qFfBaoclX3L/suAO5F9uynPyfoSoApn0wMfcPCPaCvyYfe+zK5dS33zT8I5c3+8ytgEW2KpxRxwCOfWuBPeWVkpRJsn5KdDqiaFa74+UTcRpEXUd5fhwrdASLS5fQR7xJiCXxWftN0h3iTkzVuu8+YJ4yZ9jiN7Eam/wsBQHWmo/DEKBnLELfWCcN8BCwvLJMbooBS7omWmQal67fyiQW9WsfzzptK/0eV5cu9+ggm26kYBOi/FCJV12FvuJgfUUd+k6ClbVk6ctogpZ1qzz11pTISCEkOiTWX1+quWJlb1ze0vevN1uNtA3SHW5yl//bkiGpyE3jmjtz3LlInQIgzDH60TnmTa1J8zw==";
        byte[] buff = Base64.getDecoder().decode(in);
        FileOutputStream out = new FileOutputStream("/tmp/license.bin2");
        out.write(buff);
        out.close();

        String aesKey =
            "JVQqwdRwRBQTTDHaS6N4/5OCCHx3TkQ2s81B0gew4/2dkgpBPnKd/8tucy6W1iL+6QtPU4/9bGAtEW/Ug15Uj8aKDfr+bJIRqLiPVIOiL9yZFu0G6jcFubXli1n6VMdvE8Z40vbHxzzz9WDtC/BKStJ8190nR8I5JyXpO91gcXob44CZsh78uUDLqRhu/nTqbQ3L+3393oN3RCXoXxwiHCs/mWjYw5rkDqgRa9Lp3fPfliM0oV1jVrj/xaO2xgUQ1TOLe8Q8szESRP6ThsnIz0XDfpowByFSXoLUs3iQC/+UqZvouMnV0eIBGVa+Icn213IG6fWusgOil6OP";
        byte[] aseKeyBuff = Base64.getDecoder().decode(aesKey);
        SecretKeySpec keySpec = new SecretKeySpec(aseKeyBuff, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] rawBuff = cipher.doFinal(buff);
        out = new FileOutputStream("/tmp/rawLicense.bin");
        out.write(rawBuff);
        out.close();
    }

    @Test
    public void testAES() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128, new SecureRandom());
        SecretKey secretKey = generator.generateKey();
        byte[] encoded = secretKey.getEncoded();
        FileOutputStream out = new FileOutputStream("/tmp/aes.key2");
        out.write(encoded);
        out.close();
        log.debug(CipherUtil.hexDumpEncoder.encode(encoded));
    }

}