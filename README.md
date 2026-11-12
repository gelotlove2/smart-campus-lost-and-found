# Smart Campus Lost & Found Management System

A Java-based desktop application developed using Java Swing to manage lost and found items on a college campus.

The application allows users to report lost or found items, search and filter records, update item status, and find possible matches using a rule-based matching system.

## Features

- Report Lost Items
- Report Found Items
- Search and filter items
- Update item status
- Find possible Lost and Found matches
- Store records using file handling
- Retain saved records after restarting the application
- User-friendly GUI using Java Swing

## Technologies Used

- **Programming Language:** Java
- **GUI:** Java Swing
- **Concepts:** Object-Oriented Programming (OOP)
- **Data Storage:** File Handling
- **IDE:** IntelliJ IDEA

## OOP Concepts Implemented

- **Encapsulation:** Private variables with getters and setters.
- **Inheritance:** LostItem and FoundItem inherit from Item.
- **Abstraction:** Abstract Item class and abstract methods.
- **Polymorphism:** Method overriding using getItemType().

## Project Structure

```text
SmartLostFound/
│
├── src/
│   ├── Main.java
│   ├── model/
│   │   ├── Item.java
│   │   ├── LostItem.java
│   │   └── FoundItem.java
│   │
│   ├── service/
│   │   ├── ItemManager.java
│   │   ├── MatchService.java
│   │   └── FileStorage.java
│   │
│   └── gui/
│       ├── Dashboard.java
│       ├── ItemForm.java
│       └── ItemTable.java
│
└── README.md
```

## How to Run the Project

### Requirements

- Java Development Kit (JDK)
- IntelliJ IDEA (recommended)

### Steps to Run Using IntelliJ IDEA

1. Download or clone this repository.
2. Extract the downloaded ZIP file if necessary.
3. Open IntelliJ IDEA.
4. Select **File → Open**.
5. Select the `SmartLostFound` project folder.
6. Wait for IntelliJ IDEA to load the project.
7. Make sure a suitable JDK is configured in Project Structure.
8. Open `src/Main.java`.
9. Click the green Run button next to the `main()` method.
10. The Smart Campus Lost & Found application will start.

## Data Storage

The application uses local file handling to store item records in an `items.txt` file.

Saved records are loaded when the application starts, so the data remains available after restarting the application.

## Matching System

The application uses a rule-based matching algorithm to compare Lost and Found reports based on:

- Item name
- Category
- Location

It calculates a matching score and suggests possible matches. These suggestions are not guaranteed matches and should be verified by the user.

## Future Improvements

- Database integration
- User authentication
- Image upload for items
- Email notifications
- Improved matching functionality

## Project Information

**Project Name:** Smart Campus Lost & Found Management System

**Project Type:** Java Desktop Application

**Purpose:** To make reporting and recovering lost items easier within a college campus.

---

*Developed as a Java OOP mini-project.*
