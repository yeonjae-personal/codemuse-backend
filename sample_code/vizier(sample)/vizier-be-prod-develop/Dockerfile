FROM bellsoft/liberica-openjdk-alpine-musl:21

RUN apk add gradle
WORKDIR /code

ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE
# Prepare by downloading dependencies
ADD gradlew /code/gradlew
ADD gradlew.bat /code/gradlew.bat
ADD gradle /code/gradle

# Gradle 권한 설정
RUN chmod +x ./gradlew

COPY build.gradle .
#COPY settings.gradle .
# RUN ./gradlew prep

ADD src /code/src

#어플리케이션 빌드
# RUN ./gradlew bootJar
RUN gradle build

# 빌드된 JAR 파일 복사s
RUN rm build/libs/*plain.jar
RUN cp `ls build/libs/*.jar` build/libs/app.jar

#port 열기
EXPOSE 8080
EXPOSE 6300

#CMD ["java","--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.time=ALL-UNNAMED", "-Duser.timezone=Asia/Seoul", \s
#"-Dspring.profiles.active=dev", "-XX:InitialRAMPercentage=70.0", "-XX:MaxRAMPercentage=70.0", "-XX:+UseG1GC", "-XX:MetaspaceSize=128m", "-Djava.security.egd=file:///dev/urandom", "-jar", "build/libs/app.jar"]
CMD ["java","--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.time=ALL-UNNAMED", "-Duser.timezone=Asia/Seoul", \
"-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-XX:InitialRAMPercentage=70.0", "-XX:MaxRAMPercentage=70.0", "-XX:+UseG1GC", "-XX:MetaspaceSize=128m", "-Djava.security.egd=file:///dev/urandom", "-jar", "build/libs/app.jar"]