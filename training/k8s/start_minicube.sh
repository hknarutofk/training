#!/bin/bash
set -x -e

minikube start \
  --driver=docker \
  --insecure-registry=harbor.hknaruto.com \
  --image-mirror-country=cn \
  --registry-mirror=https://r6w9c7qa.mirror.aliyuncs.com \
  --image-repository=registry.cn-hangzhou.aliyuncs.com/google_containers \
  --iso-url=https://kubernetes.oss-cn-hangzhou.aliyuncs.com/minikube/iso/minikube-v1.12.2.iso \
  --base-image=registry.cn-hangzhou.aliyuncs.com/google_containers/kicbase:v0.0.10
minikube addons enable ingress
minikube dashboard >/dev/null 2>&1 &
minikube tunnel
