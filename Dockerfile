FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY BOOT-INF/lib /app/lib
COPY META-INF /app/META-INF
COPY BOOT-INF/classes /app
ENTRYPOINT tini -- java -XX:MaxRAMFraction=2 -cp app:app/lib/* com.example.demo.DemoApplication