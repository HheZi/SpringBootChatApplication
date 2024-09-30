# Spring Boot Chat Application

## This is a small chat application written on Java, Spring, [WebSockets(Using STOMP Messaging Protocol)](https://docs.spring.io/spring-framework/reference/web/websocket.html) and SockJs for Front-End.

## Home Page 

![Home page screenshot](images/image2.png)

## Login Page 

![login page screenshot](images/image.png)

## Registration Page 

![Registration page screenshot](images/image3.png) 

## Create new group window 

![New group window screenshot](images/image4.png)

## User profile window 

![User profile  window screenshot](images/image5.png)

### Installation

1. Clone the repository to your local machine:

```bash
git clone https://github.com/HheZi/SpringBootChatApplication
```

2. Navigate to the project directory:

```bash
cd SpringBootChatApplication
```

3. Use Docker Compose:

```bash
docker compose up 
```

4. Or if you don't have a docker you can just run the jar file in [build folder](build/libs/) <ins>(Before this, you need to start the MongoDB and PostgreSQL databases.)</ins>
```bash
java -jar build/libs/Spring_Boot_Chat_Application-1.0.0.jar
```

## Contributing

Contributions are welcome! If you have suggestions for improvements or new features, feel free to submit a pull request or open an issue.

1. Fork the repository.
2. Create your feature branch (git checkout -b feature/AmazingFeature).
3. Commit your changes (git commit -m 'Add some AmazingFeature').
4. Push to the branch (git push origin feature/AmazingFeature).
5. Open a pull request.