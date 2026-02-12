FROM adoptopenjdk/openjdk8:jdk8u362-b09
WORKDIR /app

COPY target/*.jar /app/

RUN ls -l /app/

RUN mv $(ls -S /app/*.jar | head -1) /app/app.jar

ENV TZ=Asia/Shanghai
ENV JAVA_OPTS="-Dfile.encoding=utf8 -Djava.security.egd=file:/dev/.urandom"
ENV PARAMS="--server.port=9010 --spring.profiles.active=test --spring.cloud.nacos.server-addr=43.134.188.63:8848"

EXPOSE 9010

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar $PARAMS"]