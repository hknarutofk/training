package com.example.demo.util;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

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

}