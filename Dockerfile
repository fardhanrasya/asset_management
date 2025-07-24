# Build stage
FROM eclipse-temurin:24-jdk AS builder
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Create a non-root user
RUN addgroup --system javauser && adduser --system --ingroup javauser javauser
USER javauser

# Configure JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"] 