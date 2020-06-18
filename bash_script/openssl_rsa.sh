#!/bin/bash
#生成明文私钥文件:
openssl genrsa -out key.pem
openssl rsa -in key.pem -pubout -out pubkey.pem

#导出Java可识别的PKCS#8 der 格式私钥
openssl pkcs8 -topk8 -in key.pem -out key.pk8.der -nocrypt -outform der
openssl pkcs8 -topk8 -in key.pem -out key.pk8.pem -nocrypt
#导出Java可识别的X509 der格式公钥
openssl rsa -in key.pem -pubout -out pubkey.x509.der -outform der
openssl rsa -in key.pem -pubout -out pubkey.x509.pem -outform pem
