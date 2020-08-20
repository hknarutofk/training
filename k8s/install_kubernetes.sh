#!/bin/bash

# https://github.com/easzlab/kubeasz/

# export release=2.2.1
# curl -C- -fLO --retry 3 https://github.com/easzlab/kubeasz/releases/download/${release}/easzup
chmod +x ./easzup
./easzup -D -d 19.03.5 -k v1.18.2
./easzup -P
./easzup -S
docker exec -it kubeasz easzctl start-aio
