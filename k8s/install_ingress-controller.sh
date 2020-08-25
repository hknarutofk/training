#!/bin/bash
set -x -e
kubectl apply -f /etc/ansible/manifests/ingress/nginx-ingress/nginx-ingress.yaml
kubectl apply -f default-http-backend.yml
