# 🏦 Online Banking System (Java + MySQL + Maven)

A simple **Command Line Interface (CLI)** banking system built using **Java JDBC + MySQL**, managed with **Maven**.  
This project supports **customer management, account management, authentication, and transactions**.

---

## ⚙️ Project Highlights
- **Maven**: Handles dependencies like MySQL Connector and BCrypt automatically.
- **Java JDBC**: Connects to MySQL/MariaDB database.
- **CLI-based**: Easy to run and test from terminal.

## 🚀 Features

### 👤 Customer
- Create new customer with unique `cid`.
- Store customer details: `name`, `email`, `address`, `contact`, `password` (hashed with BCrypt).
- Prevent duplicate customers (same `name + email` → reuse existing `cid`).
- Update customer details.
- Delete customer (with all accounts, via `ON DELETE CASCADE`).

### 💳 Account
- Multiple accounts per customer.
- Each account has a unique account number (`2025XXXX` format).
- Store account balance.
- Deposit, withdraw, transfer money between accounts.

### 🔒 Security
- Passwords hashed with **BCrypt** (12 rounds).
- Authentication using email + password.

### 📊 Transactions
- Deposit money.
- Withdraw money (with balance check).
- Transfer between accounts.

---

## 📂 Project Structure
```
online-banking-system/ 
├── src/
│   ├── online/banking/databaseConnection/
│   │   └── DatabaseConnection.java       # Handles MySQL DB connection
│   │
│   ├── online/banking/dao/
│   │   ├── CustomerDao.java              # Interface for customer operations
│   │   ├── AccountDao.java               # Interface for account operations
│   │   ├── impl/
│   │   │   ├── CustomerDaoImpl.java      # Implementation of customer logic
│   │   │   └── AccountDaoImpl.java       # Implementation of account logic
│   │
│   ├── online/banking/model/
│   │   ├── Customer.java                 # Customer entity
│   │   └── Account.java                  # Account entity
│   │
│   ├── online/banking/service/
│   │   └── BankingService.java           # Service layer combining DAOs
│   │
│   ├── online/banking/util/
│   │   └── PasswordUtils.java            # BCrypt hashing + verification
│   │
│   └── Main.java                         # Entry point (CLI menu)
│
├── lib/
│   ├── mysql-connector-j-8.0.33.jar      # MySQL JDBC driver
│   └── jbcrypt-0.4.jar                   # BCrypt library
│
└── README.md
```


---

## 🗂️ Database Schema

### Table: `customer`
```sql
CREATE TABLE customer (
    cid INT AUTO_INCREMENT PRIMARY KEY,
    customerName VARCHAR(100) NOT NULL,
    customerEmail VARCHAR(100) NOT NULL UNIQUE,
    customerAddress VARCHAR(200),
    customerContact VARCHAR(20),
    customerPassword VARCHAR(255) NOT NULL
);
```
### Table: `account`
```sql
CREATE TABLE account (
    accountNo BIGINT PRIMARY KEY,
    balance INT NOT NULL,
    cid INT,
    FOREIGN KEY (cid) REFERENCES customer(cid)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
```

## 🛠️ Setup Instructions
### 1️⃣ Clone the Project

```bash
git clone https://github.com/your-username/online-banking-system.git
cd online-banking-system
```
### 2️⃣ Database Setup

```
CREATE DATABASE bankingsystem;
USE bankingsystem;

-- Run the table creation scripts from the Database Schema section
```

### 3️⃣ Configure Database Connection

#### Edit DatabaseConnection.java:
```declarative
String url = "jdbc:mysql://localhost:3306/DB_NAME"; // replace DB_NAME with your database name
String user = "root";       // use as your choice
String pass = "yourpassword";
```

### 4️⃣ Run the Project
```bash
javac -cp .:lib/mysql-connector-j-8.0.33.jar:lib/jbcrypt-0.4.jar src/Main.java
java -cp .:lib/mysql-connector-j-8.0.33.jar:lib/jbcrypt-0.4.jar src/Main
```

### CLI Menu Example
```
=== Online Banking System ===
1. Create New Customer + Account
2. Add Account to Existing Customer
3. Login (Email + Password)
4. Deposit Money
5. Withdraw Money
6. Transfer Money
7. View Account Details
8. Update Customer Info
9. Delete Customer
0. Exit
Enter your choice:
```
### 📝 Example Usage

#### ➕ Add Customer + Account
```declarative
addCustomerOrAccount("Alice", "alice@mail.com", "Dhaka", "017...", "pass123", 2000);
// Output: Customer Account has been created successfully with Account No: 20251234
```
#### ➕ Add Another Account for Same Customer
```declarative
addCustomerOrAccount("Alice", "alice@mail.com", "Pabna", "018...", "new123", 5000);
// Output: New account created for existing customer (cid=1) → Account No: 20254321
```
#### ❌ Delete Customer
```declarative
DELETE FROM customer WHERE cid = 1;
-- This will also delete all linked accounts automatically
```
### 📚 Technologies Used
- Java 21
- MySQL
- JDBC
- BCrypt (for password hashing)
- Maven (for dependency management)

### Dependencies in `pom.xml`
```
-  MySQL Connector/J    <!--- https://mvnrepository.com/artifact/com.mysql/mysql-connector-j/8.3.0 -->
-  BCrypt for password hashing    <!--- https://mvnrepository.com/artifact/at.favre.lib/bcrypt/0.10.2 -->
```


## 💻 Linux Setup: MariaDB + Apache + PHPMyAdmin

If you are on **Linux** and using **MariaDB**, here’s how to set up and start your database, Apache, and access PHPMyAdmin in the browser.

---

### 1️⃣ Start MariaDB Server
```bash
sudo systemctl start mariadb
sudo systemctl enable mariadb
```

### 2️⃣ Install and Start Apache + PHP
```bash
sudo apt update
sudo apt install apache2 php libapache2-mod-php php-mysql -y
sudo systemctl start apache2
sudo systemctl enable apache2
```
### 3️⃣ Install PHPMyAdmin
```bash
sudo apt install phpmyadmin -y
```
During installation, select **apache2** and configure the database for phpmyadmin with dbconfig
### 4️⃣ Access PHPMyAdmin
Open your web browser and go to:
```
http://localhost/phpmyadmin
```
Login with your MariaDB credentials.    

