FROM openjdk:21

ARG JAR_FILE=build/libs/SpringBootChatApplication-1.0.0.jar
ARG WORKDIR_PATH=/home/app

#RUN mkdir -p ${WORKDIR_PATH}

WORKDIR ${WORKDIR_PATH}

EXPOSE 8080:8080 

COPY ${JAR_FILE} app.jar

#RUN addgroup -S spring && adduser -S spring -G spring
#USER spring:spring

ENTRYPOINT ["java", "-jar", "app.jar"] 
