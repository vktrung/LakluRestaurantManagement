# Project Setup Guide

## System Requirements
- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6.0 or higher
- MySQL Server

## Installation and Configuration
1. **Clone the project from GitHub:**
   ```sh
   git clone git@github.com:Quynhnvhe163568z/LakluRestaurantManagement.git
   cd LakluRestaurantManagement
    ```
2. **Using dev profile for development:**
    - Copy the file `src/main/resources/application-example.yml` to `src/main/resources/application-local.yml` (or replace `application.yml` )
    - Change the `spring.profiles.active` property to `local` in `application.yml`.
3. **Configure the database connection:**
   - Open the `src/main/resources/application.yml` file.
   - Change the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties as per your MySQL installation.
4. **Run the application:**
   ```sh
    mvn spring-boot:run
    ```
