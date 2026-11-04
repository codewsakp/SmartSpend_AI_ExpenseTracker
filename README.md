# 💸 SmartSpend AI – AI-Powered Expense Tracker

SmartSpend AI is a full-stack expense management application built using **Java, Spring Boot, React.js, MySQL, and Spring AI**. It helps users manage daily expenses, track monthly budgets, analyze spending patterns, and gain financial insights.

The application also integrates **Ollama through Spring AI** to support AI-powered natural language expense entry and financial assistance.

## ✨ Features

- **User Authentication:** Secure registration and login using Spring Security and JWT.
- **Expense Management:** Add, view, update, and delete expenses.
- **Budget Tracking:** Create monthly budgets and monitor spending limits.
- **Dashboard & Analytics:** View expense summaries and category-wise spending through interactive charts.
- **AI Quick Add:** Enter expenses using natural language, such as `Samosa 20`, and get AI-generated expense details for confirmation.

## 🛠️ Technology Stack

| Category | Technologies |
|---|---|
| Backend : Java 24, Spring Boot |
| Database : MySQL, Spring Data JPA, Hibernate |
| Security : Spring Security, JWT, BCrypt |
| Frontend : React.js, JavaScript, Vite |
| API Communication : REST APIs, Axios |
| AI Integration : Spring AI, Ollama |
| Tools : Maven, Postman, Git, GitHub |
| Deployment : Railway |

## 🏗️ Application Architecture

```text
              React.js Frontend
                     |
                REST APIs
                  (Axios)
                     |
              Spring Boot Backend
                     |
       +-------------+-------------+
       |             |             |
  Spring Security  Spring Data   Spring AI
      + JWT        JPA/Hibernate     |
       |             |               |
       |           MySQL           Ollama
       |
   Authentication
```

 **AI Integration Note:** Ollama is currently configured and working in the local development environment. 
 The deployed Railway backend cannot connect to the Ollama instance running on the local machine. 
 Therefore, AI features currently require local execution with Ollama running.

## 🚀 Running the Project Locally

### Prerequisites
- Java JDK 24
- Node.js and npm
- MySQL Server
- Ollama
- Git

### 1. Clone the Repositories

**Backend**
```bash
git clone https://github.com/codewsakp/SmartSpend_AI_ExpenseTracker.git
```

**Frontend**
```bash
git clone https://github.com/codewsakp/SmartSpend-AI-Frontend.git
```

### 2. Configure MySQL

Create the database using MySQL Workbench:

```sql
CREATE DATABASE expense_tracker_db;
```

### 3. Configure the Backend

Open the backend project and configure the database credentials and JWT secret using environment variables.

Update `src/main/resources/application.properties` with the required local database and application settings.

### 4. Configure Ollama

Install Ollama from [ollama.com](https://ollama.com/) and download the model configured in the backend.

For example:

```bash
ollama pull llama3.2
```

Make sure Ollama is running before using AI features.

### 5. Run the Backend

Open a terminal in the backend directory:

```powershell
.\mvnw.cmd spring-boot:run
```

Backend URL: `http://localhost:8080`

### 6. Run the Frontend

Open another terminal in the frontend directory:

```bash
npm install
```

Create a `.env` file in the frontend root:

```env
VITE_API_BASE_URL=http://localhost:8080
```

Start the frontend:

```bash
npm run dev
```

Open the local URL displayed by Vite (usually `http://localhost:5173`).

Register an account and start using SmartSpend AI!

## 🌐 Live Application

- **Frontend:** [Open SmartSpend AI](https://smartspend-ai-frontend-production.up.railway.app)
- **Backend:** [Backend Deployment](https://smartspendaiexpensetracker-production.up.railway.app)

> AI functionality using Ollama is currently available in the local development environment, not through the deployed backend.

## 🔗 Project Repositories

- **Backend:** [SmartSpend AI – Backend](https://github.com/codewsakp/SmartSpend_AI_ExpenseTracker)
- **Frontend:** [SmartSpend AI – Frontend](https://github.com/codewsakp/SmartSpend-AI-Frontend)

## 🎥 Demo

Application screenshots and a complete screen-recording walkthrough will be added soon.

## 👩‍💻 Author

**GitHub:** [codewsakp](https://github.com/codewsakp)

---

*SmartSpend AI – Making personal expense management smarter.*
