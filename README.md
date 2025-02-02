
# Task Management Service 🚀

## Overview
The **Task Management Service** is a backend system designed to handle task creation, updating, retrieval, and deletion. It provides a RESTful API for efficient task management.

---

## 📖 Features
- ✅ **Task Management**: Create, update, retrieve, and delete tasks.
- ✅ **User Management**: Associate tasks with users and manage user roles.
- ✅ **Pagination & Filtering**: Retrieve tasks with pagination support.
- ✅ **Database Support**: Uses PostgreSQL as the primary database.
- ✅ **Security**: JWT authentication can be enabled.

---

## 🔧 System Requirements

Before running the service, ensure you have:

- **Java 17+**
- **Spring Boot 3+**
- **Maven**
- **PostgreSQL** (or another configured database)
- **Docker** & **Docker Compose**

---

## 🚀 Running the Service
### **1️⃣ Running Locally (With Docker)**
#### **Clone the Repository**
```sh
git clone https://github.com/Kendzyma/Task-Management.git
cd TaskManagement
docker build -t task-management-service .
docker compose up -d
```
----

## Documentation
This service is currently hosted on render and it is documented using Swagger
[online-swagger-ui](https://task-management-dyjs.onrender.com/swagger-ui/index.html)

[local-swagger-ui](http://localhost:8083/swagger-ui/index.html)

