#bước 1: Sử dụng image maven để build ứng dụng
# Sử dụng maven để build ứng dụng
FROM maven:3.9.9-amazoncorretto-21-debian AS build

# Thiết lập thư mục làm việc
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build ứng dụng
RUN mvn clean package -DskipTests

#bước 2: tạo image cho ứng dụng
# chạy với Amzon Corretto JDK 21
FROM amazoncorretto:21.0.6-alpine3.20

WORKDIR /app
COPY --from=build /app/target/*.jar /app.jar

# Chạy ứng dụng và gọi script
CMD ["sh", "-c", "java -jar /app.jar"]
