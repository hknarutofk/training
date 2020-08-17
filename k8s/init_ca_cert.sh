#!/bin/bash
set -x -e

#初始化自签名CA，密码123333
(
  cd openssl-CA
  sh CA.sh -newca
  # 生成一个通佩符证书，CN=*.hknaruto.com， openssl.cnf中指定附加属性
  sh CA.sh -newreq
  sh CA.sh -signCA
  # 导出根证书
  openssl x509 -in demoCA/cacert.pem -out ca.pem
  # 导出域名证书pem格式
  openssl x509 -in newcert.pem -out hknaruto.com.pem
  openssl rsa -in newkey.pem -out hknaruto.com.key
)
