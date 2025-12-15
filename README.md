# CV Profile Backend API

Spring Boot RESTful API cho quản lý CV Profile với WebSocket chat realtime và xuất PDF.

## Tính năng

-    ✅ Quản lý Profile (CRUD)
-    ✅ Quản lý Skills (CRUD)
-    ✅ Quản lý Projects (CRUD)
-    ✅ Quản lý Publications (CRUD)
-    ✅ Quản lý Events (CRUD)
-    ✅ Chat Realtime với WebSocket/STOMP
-    ✅ Xuất CV ra PDF
-    ✅ CORS Support
-    ✅ PostgreSQL & H2 Database

## Công nghệ

-    Java 17
-    Spring Boot 3.2.0
-    Spring Data JPA
-    Spring WebSocket
-    PostgreSQL
-    H2 Database (Development)
-    iText PDF
-    Lombok

## Cấu trúc thư mục

```
cv-profile-backend/
├── src/main/java/com/cvprofile/
│   ├── CvProfileApplication.java
│   ├── config/
│   │   ├── SecurityConfig.java
│   │   ├── WebSocketConfig.java
│   │   └── WebSocketEventListener.java
│   ├── controller/
│   │   ├── ChatController.java
│   │   ├── EventController.java
│   │   ├── PdfController.java
│   │   ├── ProfileController.java
│   │   ├── ProjectController.java
│   │   ├── PublicationController.java
│   │   └── SkillController.java
│   ├── dto/
│   │   └── ApiResponse.java
│   ├── entity/
│   │   ├── ChatMessage.java
│   │   ├── Event.java
│   │   ├── Profile.java
│   │   ├── Project.java
│   │   ├── Publication.java
│   │   └── Skill.java
│   ├── exception/
│   │   └── GlobalExceptionHandler.java
│   ├── repository/
│   │   ├── ChatMessageRepository.java
│   │   ├── EventRepository.java
│   │   ├── ProfileRepository.java
│   │   ├── ProjectRepository.java
│   │   ├── PublicationRepository.java
│   │   └── SkillRepository.java
│   └── service/
│       ├── ChatService.java
│       ├── EventService.java
│       ├── PdfExportService.java
│       ├── ProfileService.java
│       ├── ProjectService.java
│       ├── PublicationService.java
│       └── SkillService.java
└── src/main/resources/
    ├── application.properties
    └── application-dev.properties
```

## API Endpoints

### Profile APIs

-    `GET /api/profiles` - Lấy tất cả profiles
-    `GET /api/profiles/{id}` - Lấy profile theo ID
-    `GET /api/profiles/email/{email}` - Lấy profile theo email
-    `POST /api/profiles` - Tạo profile mới
-    `PUT /api/profiles/{id}` - Cập nhật profile
-    `DELETE /api/profiles/{id}` - Xóa profile

### Skills APIs

-    `GET /api/profiles/{profileId}/skills` - Lấy tất cả skills
-    `GET /api/profiles/{profileId}/skills/category/{category}` - Lấy skills theo category
-    `POST /api/profiles/{profileId}/skills` - Tạo skill mới
-    `PUT /api/profiles/{profileId}/skills/{skillId}` - Cập nhật skill
-    `DELETE /api/profiles/{profileId}/skills/{skillId}` - Xóa skill

### Projects APIs

-    `GET /api/profiles/{profileId}/projects` - Lấy tất cả projects
-    `POST /api/profiles/{profileId}/projects` - Tạo project mới
-    `PUT /api/profiles/{profileId}/projects/{projectId}` - Cập nhật project
-    `DELETE /api/profiles/{profileId}/projects/{projectId}` - Xóa project

### Publications APIs

-    `GET /api/profiles/{profileId}/publications` - Lấy tất cả publications
-    `POST /api/profiles/{profileId}/publications` - Tạo publication mới
-    `PUT /api/profiles/{profileId}/publications/{publicationId}` - Cập nhật publication
-    `DELETE /api/profiles/{profileId}/publications/{publicationId}` - Xóa publication

### Events APIs

-    `GET /api/profiles/{profileId}/events` - Lấy tất cả events
-    `POST /api/profiles/{profileId}/events` - Tạo event mới
-    `PUT /api/profiles/{profileId}/events/{eventId}` - Cập nhật event
-    `DELETE /api/profiles/{profileId}/events/{eventId}` - Xóa event

### Chat APIs

-    `WebSocket: /ws` - WebSocket endpoint
-    `STOMP: /app/chat.sendMessage` - Gửi tin nhắn
-    `STOMP: /app/chat.addUser` - Thêm user
-    `Subscribe: /topic/public` - Subscribe để nhận tin nhắn
-    `GET /api/chat/history?sender={sender}&recipient={recipient}` - Lấy lịch sử chat
-    `GET /api/chat/unread/{recipient}` - Lấy tin nhắn chưa đọc
-    `PUT /api/chat/read/{messageId}` - Đánh dấu đã đọc

### PDF Export API

-    `GET /api/pdf/export/{profileId}` - Xuất CV ra PDF

## Cài đặt và Chạy

### Prerequisites

-    Java 17+
-    Maven 3.6+
-    PostgreSQL 12+ (optional, có thể dùng H2)

### Development (H2 Database)

```bash
# Clone repository
git clone <repository-url>

# Navigate to project
cd cv-profile-backend

# Run với H2 database
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production (PostgreSQL)

```bash
# Cấu hình PostgreSQL trong application.properties
# Tạo database
createdb cvprofile

# Run application
mvn spring-boot:run
```

## Cấu hình

### application.properties

```properties
server.port=8080
spring.datasource.url=jdbc:postgresql://localhost:5432/cvprofile
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### application-dev.properties (H2)

```properties
spring.datasource.url=jdbc:h2:mem:cvprofile
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## WebSocket Client Example

```javascript
const SockJS = require("sockjs-client");
const Stomp = require("stompjs");

const socket = new SockJS("http://localhost:8080/ws");
const stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
	console.log("Connected: " + frame);

	// Subscribe to messages
	stompClient.subscribe("/topic/public", function (message) {
		console.log("Received:", JSON.parse(message.body));
	});

	// Send message
	stompClient.send(
		"/app/chat.sendMessage",
		{},
		JSON.stringify({
			sender: "user1",
			recipient: "user2",
			content: "Hello!",
			type: "CHAT",
		}),
	);
});
```

## Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn clean test jacoco:report
```

## Build

```bash
# Build JAR file
mvn clean package

# Run JAR
java -jar target/cv-profile-backend-1.0.0.jar
```

## License

MIT License
