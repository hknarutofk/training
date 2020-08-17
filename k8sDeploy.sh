#!/bin/bash
repository="harbor.hknaruto.com/yeqiang/training"

sh dockerBuild.sh
sh dockerPush.sh
(
  cd kubectl/
  kustomize edit set image "$repository":"$(git branch | awk '{print $2}')"."$(git rev-parse --short HEAD)"
  kustomize build | kubectl apply -f - -n default
)
