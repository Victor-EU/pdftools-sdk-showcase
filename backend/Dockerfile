# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY lib ./lib
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR
COPY --from=build /app/target/*.jar app.jar

# Copy native libraries and JAR from build stage
COPY --from=build /app/lib/linux-x64 /app/lib/linux-x64
COPY --from=build /app/lib/com.pdftools.jar /app/lib/

# Set native library path
ENV LD_LIBRARY_PATH=/app/lib/linux-x64
ENV NATIVE_LIB_PATH=/app/lib/linux-x64

# Create directories for uploads and outputs
RUN mkdir -p /app/uploads /app/outputs

# Railway uses PORT environment variable
ENV PORT=5001
EXPOSE 5001

ENTRYPOINT ["java", "-Djava.library.path=/app/lib/linux-x64", "-jar", "app.jar", "--server.port=${PORT}"]
