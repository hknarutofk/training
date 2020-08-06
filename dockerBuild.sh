mvn clean package -Dmaven.test.skip=true -T4
mkdir target/dependency
(
  cd target/dependency
  jar -xf ../*.jar
)
docker build -t harbor.hknaruto.com/yeqiang/training .
