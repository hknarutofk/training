#！/bin/bash
repository="harbor.hknaruto.com/library/training"

docker login harbor.hknaruto.com
docker push "$repository":"$(git branch | awk '{print $2}')"."$(git rev-parse --short HEAD)"
