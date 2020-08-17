repository="harbor.hknaruto.com/yeqiang/training"

mvn clean package -Dmaven.test.skip=true -T4
mkdir target/dependency
cp Dockerfile target/dependency
(
  cd target/dependency
  jar -xf ../*.jar
  docker build -t "$repository":"$(git branch | awk '{print $2}')"."$(git rev-parse --short HEAD)" .
  # 清理老的镜像，本地保留3个最近构建的镜像，从第4个开始，删除本地镜像
  docker images | grep "$repository" | tail -n +4 | awk '{print $3}' | xargs -i docker rmi {}
)
