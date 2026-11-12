# Smart Campus Lost & Found Management System

A beginner-friendly Java Swing desktop application for recording lost and found items on campus.

## Features
- Register lost and found reports
- View reports in a table
- Search by item name and filter by category, location, and status
- Update a report's status to Lost, Found, or Returned
- Save and load records using the local `items.txt` file
- Suggest potential Lost/Found matches using a simple rule-based score
- Show counts for Lost, Found, and Returned statuses
- Validate item names, locations, and dates

## Requirements
- Java JDK (JDK 17 or newer recommended)
- IntelliJ IDEA or another Java IDE

## Run in IntelliJ IDEA
1. Open the `SmartLostFound` folder as a project.
2. Ensure `src` is marked as the Sources Root if IntelliJ does not recognize it automatically.
3. Open `src/Main.java` and run `Main.main()`.
4. Records are stored in `items.txt` in the application's working directory.

## How matching works
Select a row in the table and click **Find Matches**. The program compares the name, category, and location of the selected report with reports of the opposite type (Lost vs Found). It displays a score from 0 to 100. These are suggestions only; the user must verify any possible match.

## Manual test checklist
1. Register one Lost item and one Found item with the same name, category, and location.
2. Search by part of the name and test each filter.
3. Select a row and click **Find Matches**.
4. Select a row and update its status.
5. Close and reopen the application; confirm records and status changes remain saved.
6. Try saving a blank name or location and an invalid date; confirm validation messages appear.
