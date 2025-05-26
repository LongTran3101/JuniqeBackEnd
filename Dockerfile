# Sử dụng image chính thức của OpenJDK 21 trên Alpine Linux
FROM openjdk:21

# Cấu hình các tham số để cài đặt và ghi log tốt hơn
ENV JAVA_OPTS=""

# Tạo thư mục app
WORKDIR /app

# Copy file JAR đã build vào container
COPY target/juniqe-1.0.0.jar app.jar

# Expose port ứng dụng (Spring Boot mặc định là 8080)
EXPOSE 8080

# Lệnh chạy khi container khởi động
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
