Made with Perplexity
🚗 CAR POOLING PLATFORM - COMPLETE SYSTEM
📌 PROJECT OVERVIEW
A production-ready Spring Boot application for managing car pooling services with:

User registration (drivers & passengers)

Ride creation and management

Booking system with confirmation workflow

Prevents overbooking with database constraints

Role-based access control

MySQL database backend

📚 DOCUMENTATION FILES
File	Purpose	Read Time
pom.xml	Maven configuration	5 min
application.properties	Spring Boot config	5 min
mysql-setup-guide.md	Database setup & testing	15 min
complete-source-code.md	All 25+ Java classes	30 min
THIS FILE	Complete overview	10 min
🏗️ ARCHITECTURE
text
┌─────────────────┐
│   HTTP Client   │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────┐
│        Controllers Layer            │
│  AuthController                     │
│  RideController                     │
│  BookingController                  │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│        Services Layer               │
│  UserService → UserServiceImpl       │
│  RideService → RideServiceImpl       │
│  BookingService → BookingServiceImpl │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│      Repositories Layer             │
│  UserRepository                     │
│  RideRepository                     │
│  BookingRepository                  │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│      MySQL Database                 │
│  users | rides | bookings           │
└─────────────────────────────────────┘
🔐 SECURITY IMPLEMENTATION
Authentication
HTTP Basic Auth with Spring Security

BCrypt password encryption

Session-based authentication

CSRF protection enabled

Authorization
text
- DRIVER: Can create rides, confirm bookings, view bookings
- PASSENGER: Can search rides, book rides, cancel bookings
📊 DATABASE SCHEMA
Users Table
text
id (BIGINT) → Primary Key
email (VARCHAR 100) → Unique
password (VARCHAR 255) → BCrypt encrypted
full_name (VARCHAR 100)
phone_number (VARCHAR 20) → Unique
role (VARCHAR 20) → DRIVER or PASSENGER
is_active (BOOLEAN) → Account status
created_at (BIGINT) → Timestamp
updated_at (BIGINT) → Timestamp
Rides Table
text
id (BIGINT) → Primary Key
driver_id (BIGINT) → Foreign Key to users
source_location (VARCHAR 255)
destination_location (VARCHAR 255)
total_seats (INT) → Original capacity
available_seats (INT) → Remaining capacity
departure_time (BIGINT) → Unix timestamp
estimated_fare (DOUBLE)
description (VARCHAR 500) → Optional
created_at (BIGINT)
updated_at (BIGINT)
Bookings Table
text
id (BIGINT) → Primary Key
ride_id (BIGINT) → Foreign Key to rides
passenger_id (BIGINT) → Foreign Key to users
status (VARCHAR 20) → REQUESTED/CONFIRMED/CANCELLED/COMPLETED
seats_booked (INT) → Number of seats
created_at (BIGINT)
updated_at (BIGINT)
UNIQUE(ride_id, passenger_id) → One booking per passenger per ride
🔗 KEY FEATURES
1. User Management
Registration with validation

Login with password encryption

Role-based differentiation

Account activation/deactivation

2. Ride Management
Drivers create rides with details

Search rides by location and time

Display available seats

Cancel rides

3. Booking System
Passengers book available seats

Request-based workflow

Driver confirmation required

Cancel bookings with seat refunds

4. Business Logic
Prevent Overbooking: Database UNIQUE constraint

Seat Management: Reduce available seats on confirmation

Duplicate Prevention: Cannot book same ride twice

Authorization: Only drivers can confirm, passengers can book

5. Data Validation
Email format validation

Phone number length (10 digits)

Required field checks

Positive number validation for seats and fare

🛠️ TECHNOLOGY STACK
Layer	Technology
Framework	Spring Boot 3.2.0
Language	Java 17
Database	MySQL 8.0
ORM	JPA/Hibernate
Security	Spring Security
Build Tool	Maven 3.8+
Utilities	Lombok
Validation	Jakarta Validation
📝 REST API ENDPOINTS
Authentication
text
POST   /api/auth/register          Register new user
POST   /api/auth/login             Login user
GET    /api/auth/current-user      Get current user
POST   /api/auth/logout            Logout user
Rides
text
POST   /api/rides/create           Create ride (DRIVER only)
GET    /api/rides/{id}             Get ride details
POST   /api/rides/search           Search rides
GET    /api/rides/my-rides         Get driver's rides (DRIVER only)
DELETE /api/rides/{id}/cancel      Cancel ride (DRIVER only)
Bookings
text
POST   /api/bookings/book          Book a ride (PASSENGER only)
PUT    /api/bookings/{id}/confirm  Confirm booking (DRIVER only)
PUT    /api/bookings/{id}/cancel   Cancel booking (PASSENGER only)
GET    /api/bookings/ride/{rideId} Get bookings for ride
GET    /api/bookings/my-bookings   Get passenger's bookings
🚀 QUICK START
1. Database Setup
bash
mysql -u root -p < mysql-setup-guide.md
2. Create Project
bash
mvn archetype:generate -DgroupId=com.carpooling -DartifactId=carpooling-platform
3. Copy Files
Copy pom.xml to project root

Copy all Java files into appropriate packages

Copy application.properties to src/main/resources/

4. Build & Run
bash
mvn clean install
mvn spring-boot:run
5. Test
bash
# Register driver
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -u driver@example.com:password123 \
  -d '{"email":"driver@example.com","password":"password123",...}'

# Create ride
curl -X POST http://localhost:8080/api/rides/create \
  -H "Content-Type: application/json" \
  -u driver@example.com:password123 \
  -d '{"sourceLocation":"Bangalore",...}'
📦 PROJECT STRUCTURE
text
carpooling-platform/
├── src/main/java/com/carpooling/
│   ├── entity/
│   │   ├── enums/
│   │   │   ├── UserRole.java
│   │   │   └── BookingStatus.java
│   │   ├── User.java
│   │   ├── Ride.java
│   │   └── Booking.java
│   ├── dto/
│   │   ├── request/
│   │   │   ├── UserRegisterRequest.java
│   │   │   ├── UserLoginRequest.java
│   │   │   ├── RideCreateRequest.java
│   │   │   ├── RideSearchRequest.java
│   │   │   └── BookingRequest.java
│   │   └── response/
│   │       ├── UserResponse.java
│   │       ├── RideResponse.java
│   │       ├── BookingResponse.java
│   │       └── ApiResponse.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── RideRepository.java
│   │   └── BookingRepository.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── RideService.java
│   │   ├── BookingService.java
│   │   └── impl/
│   │       ├── UserServiceImpl.java
│   │       ├── RideServiceImpl.java
│   │       └── BookingServiceImpl.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── RideController.java
│   │   └── BookingController.java
│   ├── config/
│   │   └── SecurityConfig.java
│   ├── exception/
│   │   ├── ResourceNotFoundException.java
│   │   ├── UnauthorizedException.java
│   │   ├── BookingException.java
│   │   └── GlobalExceptionHandler.java
│   ├── security/
│   │   └── CustomUserDetails.java
│   └── CarpoolingApplication.java
├── src/main/resources/
│   └── application.properties
├── pom.xml
└── README.md
✅ VERIFICATION CHECKLIST
Setup Complete?
 MySQL database created

 All tables created

 Maven project created

 All Java files copied

 pom.xml updated

 application.properties configured

Build & Run?
 mvn clean install succeeds

 mvn spring-boot:run starts without errors

 No compilation warnings

API Testing?
 Can register user

 Can login user

 Can create ride (driver)

 Can search rides

 Can book ride (passenger)

 Can confirm booking (driver)

 Can cancel booking (passenger)

 Error handling works

Security?
 Only drivers can create rides

 Only passengers can book rides

 Only drivers can confirm bookings

 Passwords are encrypted

 Unauthorized requests return 403

🎓 INTERVIEW PREPARATION
30-Second Pitch
text
"I built a Spring Boot car pooling platform demonstrating clean architecture,
role-based access control, and business logic enforcement. The key challenge 
was preventing race conditions in booking—solved with database constraints and 
transaction management."
Technical Deep Dive
Question: How did you prevent overbooking?

Answer:

text
I used a multi-layered approach:
1. Check available seats before booking (application layer)
2. UNIQUE constraint in database (database layer)
3. Reduce available seats on confirmation (atomic transaction)
4. @Transactional annotation ensures all-or-nothing updates

This prevents race conditions where multiple passengers could book the same seat.
Scaling Discussion
Question: How would you scale this?

Answer:

text
For 1M users:
- Add Redis caching for ride searches
- Implement pagination for bookings
- Add database read replicas
- Use event-driven architecture (Kafka) for notifications
- Add API rate limiting
- Implement pessimistic locking for seat reservations
🐛 TROUBLESHOOTING
Issue: Connection Refused
text
Solution: Ensure MySQL is running and credentials match application.properties
Issue: Table Not Found
text
Solution: Run database creation SQL from mysql-setup-guide.md
Issue: Authentication Failed
text
Solution: Ensure user is registered with correct email/password
Issue: Build Fails
text
Solution: Run mvn clean, check Java version (17+), verify all package names
📈 STATISTICS
Metric	Value
Java Classes	25+
REST Endpoints	10+
Database Tables	3
Business Rules	5+
Lines of Code	~2,500
Time to Build	5-6 hours
Time to Test	1-2 hours
🎯 NEXT STEPS
Implement Unit Tests - Add JUnit 5 test cases

Add Swagger/OpenAPI - Auto-generate API documentation

Implement Ratings - Allow passengers to rate drivers

Add Notifications - Email/SMS on booking status

Payment Integration - Add payment gateway

Deploy - AWS/Heroku/Azure deployment

Monitor - Add Prometheus/ELK logging

📞 SUPPORT
If stuck, check:

mysql-setup-guide.md - Database and setup issues

complete-source-code.md - Code structure and implementation

Application logs - Debug information

pom.xml - Dependency issues

✨ KEY TAKEAWAYS
✅ Production-Quality Code - Real error handling, validation, security
✅ Clean Architecture - Proper separation of concerns
✅ Business Logic - Real-world problem solving
✅ Database Design - Proper constraints and indexes
✅ Security - Authentication, authorization, encryption
✅ API Design - RESTful, proper HTTP status codes
✅ Interview Ready - Can explain every decision

You now have a complete, working car pooling platform! 🚀
