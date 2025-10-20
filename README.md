# Isthmus AI - Backend (minimal starter)

## Requirements
- Java 17+
- Maven
- MySQL running at jdbc:mysql://localhost:3306/isthmus_ai (user=root, password=root)

## Run
1. Create database: `CREATE DATABASE isthmus_ai;`
2. Build: `mvn clean package`
3. Run: `mvn spring-boot:run`
4. Health: http://localhost:8080/api/hello
