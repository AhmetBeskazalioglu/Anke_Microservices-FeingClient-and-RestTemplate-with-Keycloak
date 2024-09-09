# OpenJDK 22'nin Alpine versiyonunu temel imaj olarak kullanıyoruz
FROM openjdk:22-jdk

# Çalışma dizinini oluşturuyoruz
WORKDIR /app

# Uygulama jar dosyasını konteynıra kopyalıyoruz
COPY target/discovery-server-0.0.1-SNAPSHOT.war /app/discovery-server.war

# Uygulama için gerekli portu açıyoruz
EXPOSE 8080

# Spring Boot uygulamasını çalıştırmak için komut
CMD ["java", "-jar", "discovery-server.war"]