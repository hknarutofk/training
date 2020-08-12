#!/bin/bash
set -x -e

curl -LO https://storage.googleapis.com/minikube/releases/v1.12.1/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
sudo dnf install kubernetes-client
