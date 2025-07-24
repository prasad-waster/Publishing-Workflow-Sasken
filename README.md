<<<<<<< HEAD
# 📘 BlogCraft — Publishing Workflow Module

This is the core module of the **BlogCraft** platform — an internal blog creation and publishing system designed for collaborative content workflows. The Publishing Workflow module handles the entire lifecycle of a blog post: from draft creation to review, approval, and final publishing.

## 🛠️ Tech Stack

- **Frontend:** HTML, Tailwind CSS, JavaScript (Vanilla)
- **Backend:** Spring Boot (Java 17), REST APIs
- **Database:** MySQL
- **Build Tool:** Maven
- **IDE:** Visual Studio Code

---

## 🚀 Features

### ✅ Core Workflow

- **Draft Creation:** Save drafts and send for review
- **Review Panel:** Approve/reject posts with comments
- **Publishing Dashboard:** Final approval and publishing control
- **Status Tracking:** DRAFT → REVIEW → APPROVED → PUBLISHED
- **Comment System:** Reviewers can leave feedback
- **Status History:** Track post lifecycle
- **Draft Management:** Authors can edit or delete drafts

---

## 🔁 Workflow Overview

```
DRAFT → REVIEW → APPROVED → PUBLISHED  
       ↘ ← ← ← ← ← ← ← ← ← ← ← ← ← ↙  
```

---

## 🧱 Module Pages & Scripts

| Page              | Script        | Description                                 |
|------------------|---------------|---------------------------------------------|
| `create.html`     | —             | Draft creation interface                    |
| `review.html`     | `review.js`   | Reviewer panel to approve/reject posts      |
| `dashboard.html`  | `dashboard.js`| Admin dashboard for publishing              |
| `posts.html`      | `posts.js`    | View all posts, status history, comments    |
| `drafts.html`     | `drafts.js`   | Manage drafts (edit/delete)                 |
| `edit.html`       | `edit.js`     | Edit draft and update status                |

---

## 🔗 API Endpoints

| Method | Endpoint                          | Description                        |
|--------|-----------------------------------|------------------------------------|
| POST   | `/api/posts`                      | Create a new draft post            |
| PUT    | `/api/posts/{id}/status`          | Change status of a post            |
| PUT    | `/api/posts/{id}`                 | Update post content                |
| GET    | `/api/posts`                      | Fetch all posts                    |
| GET    | `/api/posts/{id}`                 | Fetch a single post                |
| GET    | `/api/posts/{id}/comments`        | Get review comments                |
| POST   | `/api/posts/{id}/comment`         | Add a review comment               |
| GET    | `/api/posts/{id}/history`         | Get status history of a post       |
| GET    | `/api/posts/status-summary`       | Dashboard metrics                  |

---

## 👤 Roles & Permissions

- **Author:**  
  Create, edit, delete drafts; submit for review

- **Reviewer:**  
  View submitted posts, approve/reject with comments

- **Admin:**  
  Final publishing and visibility control

---

## 📌 Future Enhancements (Planned)

- Secure login with role-based authentication  
- Rich text editing  
- Tagging and categorization  
- Email or Slack-based notifications  
- Author analytics and blog performance stats

---

## 📦 How to Run the Project

1. **Clone the repo**  
   ```bash
   git clone https://github.com/your-username/blogcraft-workflow.git
   cd blogcraft-workflow
   ```

2. **Backend Setup**
   - Import the Spring Boot project in VS Code or IntelliJ
   - Set up MySQL and update `application.properties`
   - Run the project using Maven or your IDE

3. **Frontend Setup**
   - Open the HTML files in a browser (`create.html`, `review.html`, etc.)
   - Connect to the backend via API endpoints

---

## 📬 Contact

For questions or walkthroughs, feel free to contact:  
📧 **Prasad Waster** — https://www.linkedin.com/in/prasadwaster/
=======
# 🤝 Collaboration-Module-Sasken (BlogCraft)

This module enables **collaborative blog drafting** within the BlogCraft platform. Built using **Spring Boot** and **MySQL**, it supports real-time co-authoring, suggestions, and commenting to enhance team-driven content creation.

---

## 🌟 Collaboration Features

🔧 **Collaboration Tools for Drafts**  
- ✏️ **Edit Draft** – Co-authors can modify existing draft content.  
- 💡 **Suggest Changes** – Team members can give suggestions before finalization.  
- 💬 **Comment System** – Users can add feedback or comments tied to a specific draft.  
- 🔄 Real-time updates reflected in the database (`drafts` and `comments` tables).

---

## 🛠️ Technologies Used

- **Backend**: Java, Spring Boot (RESTful APIs)
- **Frontend**: HTML, JavaScript, Tailwind CSS
- **Database**: MySQL + Spring Data JPA

---

## 📁 Folder Highlights

collabtool/
├── controller/
│ └── CollaborationController.java
├── model/
│ ├── Draft.java
│ └── Comment.java
├── repository/
│ ├── DraftRepository.java
│ └── CommentRepository.java
├── resources/
│ └── static/ (Frontend files like drafts.html, comments.html, JS files)
└── application.properties

---

## 💡 How It Works

- Drafts are saved by authors and viewable by collaborators.
- Collaborators can **edit** or **comment** on those drafts.
- Each comment includes a `username` and a `message`.
- Data is persisted in MySQL via Spring Boot repositories.

---

## Screenshots

## 🖼️ Screenshots

### 🧾 BlogCraft Overview
![BlogCraft](blogcraft.png)

### 🤝 Collaboration Tool UI
![Collaboration Tool](collabtool.png)

### 💬 Comments Interface
![Comments](comments.png)

### ✍️ Draft Creation
![Draft](draft.png)

---

## Authors

1. Vishakha Yeole
2. Mayur Bhong
3. Mayur Aghao







>>>>>>> collab/main
