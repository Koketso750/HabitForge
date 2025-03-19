# HabitForge

## Overview
HabitForge is a habit-tracking web application designed to help users develop and maintain healthy habits while promoting overall wellness. The system provides a task management system with a calendar view, access to wellness content, user feedback options, and multimedia uploads for coaches.

## Features
- **Task Management System**: Users can create, update, and delete tasks while managing them through a calendar view.
- **Wellness Content**: Provides users with access to wellness-related materials covering mental, physical, emotional, and self-care topics.
- **User Feedback**: Users can leave feedback on wellness content.
- **Coach Contributions**: Registered coaches can upload multimedia wellness content and optionally review user feedback.
- **Notifications**: Users receive notifications for upcoming tasks.
- **Email Notifications**: System sends email confirmations upon user and coach registration.

## User Roles
1. **Registered User**:
   - Access task management system via the calendar
   - View, create, update, and delete tasks
   - Access wellness content
   - Leave feedback on content
2. **Registered Coach**:
   - Upload multimedia wellness content
   - View user feedback (optional)
3. **Guest**:
   - View wellness content only

## System Architecture
HabitForge follows a three-layered architecture:
1. **Presentation Layer**: HTML, JavaScript, Bootstrap, Spring with Thymeleaf
2. **Business Layer**: Spring Framework
3. **Persistent Layer**: MySQL (HabitForgeDB)

## Installation and Setup
### Prerequisites
- Java (JDK 17 or later)
- Spring Boot
- MySQL Database
- Maven

### Steps to Run
1. **Clone the Repository**:
   ```sh
   git clone https://github.com/koketso750/HabitForge.git
   cd HabitForge
   ```
2. **Configure the Database**:
   - Create a MySQL database named `HabitForgeDB`.
   - Update `application.properties` with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/habitforgedb
     spring.datasource.username=root
     spring.datasource.password=root
     ```
3. **Build and Run the Application**:
   ```sh
   mvn clean install
   mvn spring-boot:run
   ```
4. **Access the Application**:
   Open `http://localhost:8080` in a web browser.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -m 'Add new feature'`).
4. Push to your branch (`git push origin feature-branch`).
5. Submit a pull request.

---
Happy habit-building with HabitForge! 🚀

