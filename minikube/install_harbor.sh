#!/bin/bash
set -x -e
# 安装ingress需要的tls证书，注意ingress不支持三级域名使用通佩服证书
# 计划域名 harbor.hknaruto.com
kubectl create namespace harbor
(
  cd openssl-CA
  kubectl create -n harbor secret tls hknaruto.com --cert=hknaruto.com.pem --key=hknaruto.com.key
)
(
  cd harbor
  helm install -n harbor harbor .
)
