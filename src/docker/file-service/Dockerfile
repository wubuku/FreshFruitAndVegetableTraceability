# 构建阶段
FROM eclipse-temurin:17-jdk-jammy as build

WORKDIR /opt/ffvtraceability

# 复制项目文件 - 路径相对于 context (src)
COPY pom.xml .
COPY ffvtraceability-file-service ffvtraceability-file-service

# 使用Maven进行构建
RUN --mount=type=cache,target=/root/.m2 \
    apt-get update && \
    apt-get install -y maven && \
    mvn clean package -pl ffvtraceability-file-service -am -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre-jammy

# 创建非root用户
RUN groupadd -r ffvapp && useradd -r -g ffvapp ffvapp

# 创建临时目录
RUN mkdir /tmp/ffvapp && chown ffvapp:ffvapp /tmp/ffvapp
VOLUME /tmp/ffvapp

USER ffvapp
WORKDIR /app

# 复制构建产物
COPY --from=build --chown=ffvapp:ffvapp \
    /opt/ffvtraceability/ffvtraceability-file-service/target/ffvtraceability-file-service-*.jar \
    /app/app.jar

# 配置启动命令
ENTRYPOINT ["java", \
    "-XX:TieredStopAtLevel=1", \
    "-Dspring.main.lazy-initialization=true", \
    "-jar", "/app/app.jar" \
]

EXPOSE 8080 