#!/bin/bash
set -x -e
# 使用kustomize方法，初次安装会应为存储还为建立导致失败
# kubectl apply -k mysql/
(
  cd mysql/
  kubectl apply -f persistentVolume.yml
  kubectl apply -f deployment.yml
  kubectl apply -f service.yml
)
