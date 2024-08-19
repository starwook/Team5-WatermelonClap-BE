FROM  openjdk:17

#RUN sed -i 's/profiler.transport.grpc.collector.ip=127.0.0.1/profiler.transport.grpc.collector.ip=10.1.11.207/g' pinpoint-agent-2.5.3/pinpoint-root.config
#RUN sed -i 's/profiler.collector.ip=127.0.0.1/profiler.collector.ip=10.1.11.207/g' pinpoint-agent-2.5.3/pinpoint-root.config
COPY build/libs/lottery-0.0.1-SNAPSHOT.jar watermelon.jar
COPY pinpoint-agent-2.5.3/ /usr/local


ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=deploy
#ENTRYPOINT ["java","-jar","/watermelon.jar"]
ENTRYPOINT ["java","-jar", \
"-javaagent:/usr/local/pinpoint-bootstrap.jar", \
"-Dpinpoint.applicationName=lottery-api", \
"-Dpinpoint.config=/usr/local/pinpoint-root.config", \
"/watermelon.jar"]