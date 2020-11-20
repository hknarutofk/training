#!/bin/bash
set -x -e
# 初始化namespace
kubectl create namespace harbor

# 初始化本地部署目录
sudo mkdir -p /k8s-persistent-volume/harbor-registry
sudo mkdir -p /k8s-persistent-volume/harbor-chartmuseum
sudo mkdir -p /k8s-persistent-volume/harbor-jobservice
sudo mkdir -p /k8s-persistent-volume/harbor-database
sudo mkdir -p /k8s-persistent-volume/harbor-redis
sudo mkdir -p /k8s-persistent-volume/harbor-trivy

# 安装ingress需要的tls证书，注意ingress不支持三级域名使用通佩服证书
# 计划域名 harbor.hknaruto.com

(
  cd openssl-CA
  kubectl create -n harbor secret tls hknaruto.com --cert=hknaruto.com.pem --key=hknaruto.com.key
)
(
  cd harbor
  kubectl apply -f persistentVolume4harbor.yml
  helm install -n harbor harbor .

  # 手动安装trivy, 原程序trivy pv绑定有问题
)
