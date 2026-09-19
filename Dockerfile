# Step 1: Build application with Maven & JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run application on Tomcat 10
FROM tomcat:10.1-jdk17-temurin
WORKDIR /usr/local/tomcat

# Remove default ROOT application
RUN rm -rf webapps/ROOT

# Disable Tomcat shutdown port to prevent Render health check warnings
RUN sed -i 's/port="8005"/port="-1"/g' conf/server.xml

# Copy built WAR file as ROOT.war so the app is served at the root URL /
COPY --from=build /app/target/CatClinicProject-1.0-SNAPSHOT.war webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
