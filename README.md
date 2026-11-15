# Blood Bank Management System

A Java 25 desktop mini project for managing blood donors, available blood units, hospital requests, donation history and low-stock reporting. It uses Swing for the desktop interface, JDBC for persistence and MySQL 8 for storage. The code is organized by responsibility so that the OOP concepts are easy to trace during a semester project viva.

## Features

- Admin login with SHA-256 password hash verification
- Donor registration, editing, deletion, validation and search
- Eight blood-group stock cards with low-stock colour indicators
- Blood request creation, compatibility checks, approval and rejection
- Transactional stock issue during request approval
- Donation history search with optional start/end dates and low-stock CSV export
- Java2D stock chart and a background expiry monitor
- Prepared statements, foreign keys, indexes and packaged database configuration

## Tech stack

Java 25, Swing, JDBC, MySQL 8, Maven, Java2D.

## Screenshots

Add application screenshots to `docs/screenshots/` and embed them here before publishing. Suggested captures: login screen, dashboard, donor registration, stock cards, request processing and low-stock report.

## Setup

1. Install JDK 25, Maven 3.8+ and MySQL 8.
2. Create the database schema by running `schema.sql` in MySQL Workbench or the MySQL CLI.
3. Load the optional demonstration records by running `sample_data.sql`.
4. Copy `src/main/resources/db.properties.example` to `src/main/resources/db.properties` and enter the local JDBC URL, username and password. The real `db.properties` file is ignored by Git; do not commit credentials.
5. From the project root, run `mvn compile exec:java`.

The demo administrator is `admin` / `admin123`. Change this credential and its hash before using the application beyond a local demonstration.

## Database import

```bash
mysql -u root -p < schema.sql
mysql -u root -p < sample_data.sql
```

## Run

```bash
mvn compile exec:java
```

The application expects MySQL to be running and the `blood_bank` database to be imported. The default JDBC settings are in `src/main/resources/db.properties`.

## Project structure

```text
src/main/java/bloodbank
├── Main.java
├── core/          Domain models, inheritance, interface and blood groups
├── db/            JDBC connection and DAO classes
├── exception/     Domain-specific checked exceptions
├── service/       Validation, compatibility, stock and request workflows
├── thread/        Background stock expiry monitor
├── ui/            Swing frames, panels, theme and Java2D chart
└── util/           Validation, date formatting and CSV export
src/main/resources/db.properties
schema.sql
sample_data.sql
VIVA_NOTES.md
```

## Team members

- Sarika
- Aman Kumar
- Tanya
- Manraj

## Future scope

- Role-based access for hospital staff and administrators
- Password reset and stronger password hashing with a per-user salt
- Donor appointment scheduling and SMS/email notifications
- Audit logging, configurable retention policies and multi-user access controls
- Blood component inventory, multi-branch support and automated backups
- Automated unit and integration tests with a disposable test database

## Important project notes

This is an academic desktop application, not a certified clinical inventory system. Compatibility rules are simplified for red-cell transfusion teaching; actual transfusion decisions require cross-matching and qualified medical staff. Donation recording updates donor eligibility date, donation history and individual stock units in one database transaction. Stock issue and request approval run in a database transaction. Keep credentials outside version control and replace the demonstration admin password before sharing a live database.

## Suggested Git commit plan

1. `set up maven project and package structure`
2. `add blood group and person domain models`
3. `implement donor eligibility and validation`
4. `add custom blood bank exceptions`
5. `configure mysql connection properties`
6. `implement donor and stock data access`
7. `add transactional request processing dao`
8. `build donor stock and request services`
9. `add blood compatibility rules and reports`
10. `implement background expiry monitor`
11. `create admin login and dashboard navigation`
12. `add donor stock and request swing panels`
13. `add donation history csv export and stock chart`
14. `add database schema and demonstration records`
15. `write setup guide and oop viva notes`
