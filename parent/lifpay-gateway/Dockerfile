# 多阶段构建： 构建层+运行层，大幅减小镜像体积
FROM maven:3.8.5-openjdk-8 AS builder
WORKDIR /build
COPY parent/${APP_NAME}/pom.xml /build/
COPY parent/pom.xml /build/../

# 提前下载依赖，利用构建缓存
RUN mvn dependency:go-offline -B
COPY parent/${APP_NAME}/src /build/src
# 打包生成JAR包
RUN mvn package -DskipTests && cp target/*.jar /app.jar



FROM adoptopenjdk/openjdk8:jdk8u362-b09-slim
WORKDIR /app

COPY --from-builder /app.jar /app/app.jar

ENV TZ=Asia/Shanghai
ENV LANG=C.UTF-8
ENV JAVA_OPTS="-Dfile.encoding=utf8 -Djava.security.egd=file:/dev/.urandom -Xms512m -Xmx1024m"
ENV PARAMS="--server.port=9001 --spring.profiles.active=test --spring.cloud.nacos.server-addr=43.134.188.63:8848"

EXPOSE 9001

ENTRYPOINT ["java"]
CMD ["$JAVA_OPTS","-jar","/app/app.jar","$PARAMS"]