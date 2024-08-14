FROM  openjdk:17
COPY ./lottery/build/libs/lottery-0.0.1-SNAPSHOT.jar watermelon.jar

ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=deploy
ENTRYPOINT ["java","-jar","/watermelon.jar"]