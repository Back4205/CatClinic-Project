-- =============================================================
-- DATABASE RE-CREATION SCRIPT
-- =============================================================

USE master;
GO

IF DB_ID('pet_clinic') IS NOT NULL
BEGIN
    ALTER DATABASE pet_clinic SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE pet_clinic;
END
GO

CREATE DATABASE pet_clinic;
GO

USE pet_clinic;
GO

-- =========================
-- 1. Roles
-- =========================
CREATE TABLE Roles (
    RoleID INT IDENTITY(1,1) PRIMARY KEY,
    RoleName NVARCHAR(100) NOT NULL
);

-- =========================
-- 2. Users
-- =========================
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    UserName NVARCHAR(100) NOT NULL UNIQUE,
    Password NVARCHAR(255) NOT NULL,
    FullName NVARCHAR(150),
    Male BIT, -- 1: Male, 0: Female
    Email NVARCHAR(150) UNIQUE,
    RoleID INT,
    Active BIT DEFAULT 1,
    Phone NVARCHAR(20),

    CONSTRAINT FK_Users_Roles
    FOREIGN KEY (RoleID) REFERENCES Roles(RoleID)
);

-- =========================
-- 3. Staff
-- =========================
CREATE TABLE Staff (
    StaffID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT NOT NULL UNIQUE,
    Position NVARCHAR(100), -- e.g., Receptionist, Nurse

    CONSTRAINT FK_Staff_Users
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- =========================
-- 4. Owners
-- =========================
CREATE TABLE Owners (
    OwnerID INT IDENTITY(1,1) PRIMARY KEY,
    Address NVARCHAR(255),
    UserID INT NOT NULL UNIQUE,

    CONSTRAINT FK_Owners_Users
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- =========================
-- 5. Cats
-- =========================
CREATE TABLE Cats (
    CatID INT IDENTITY(1,1) PRIMARY KEY,
    OwnerID INT NOT NULL,
    Name NVARCHAR(100),
    Gender BIT, -- CHANGED TO BIT (1: Male, 0: Female)
    Breed NVARCHAR(100),
    Age INT,

    CONSTRAINT FK_Cats_Owners
    FOREIGN KEY (OwnerID) REFERENCES Owners(OwnerID)
);

-- =========================
-- 6. Veterinarians
-- =========================
CREATE TABLE Veterinarians (
    VetID INT IDENTITY(1,1) PRIMARY KEY,
    Degree NVARCHAR(100),
    ExperienceYear INT,
    UserID INT NOT NULL UNIQUE,

    CONSTRAINT FK_Vets_Users
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- =========================
-- 7. TimeSlots
-- =========================
CREATE TABLE TimeSlots (
    TimeSlotID INT IDENTITY(1,1) PRIMARY KEY,
    VeterinarianID INT NOT NULL,
    StartTime TIME,
    EndTime TIME,
    Date DATE,
    Status NVARCHAR(50), -- Available, Booked

    CONSTRAINT FK_TimeSlots_Vets
    FOREIGN KEY (VeterinarianID) REFERENCES Veterinarians(VetID)
);

-- =========================
-- 8. Service
-- =========================
CREATE TABLE Service (
    ServiceID INT IDENTITY(1,1) PRIMARY KEY,
    NameService NVARCHAR(150),
    Price DECIMAL(10,2),
    Description NVARCHAR(255),
    TimeService INT, -- In minutes
    IsActive BIT DEFAULT 1
);

-- =========================
-- 9. Booking
-- =========================
CREATE TABLE Booking (
    BookingID INT IDENTITY(1,1) PRIMARY KEY,
    CatID INT NOT NULL,
    VeterinarianID INT NOT NULL,
    Status NVARCHAR(50), -- Pending, Completed, Cancelled
    AppointmentDate DATETIME,

    CONSTRAINT FK_Booking_Cats
    FOREIGN KEY (CatID) REFERENCES Cats(CatID),

    CONSTRAINT FK_Booking_Vets
    FOREIGN KEY (VeterinarianID) REFERENCES Veterinarians(VetID)
);

-- =========================
-- 10. Appointment_Service (Junction Table)
-- =========================
CREATE TABLE Appointment_Service (
    BookingID INT NOT NULL,
    ServiceID INT NOT NULL,

    CONSTRAINT PK_Appointment_Service
    PRIMARY KEY (BookingID, ServiceID),

    CONSTRAINT FK_AS_Booking
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID),

    CONSTRAINT FK_AS_Service
    FOREIGN KEY (ServiceID) REFERENCES Service(ServiceID)
);

-- =========================
-- 11. MedicalRecords
-- =========================
CREATE TABLE MedicalRecords (
    MedicalRecordID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID INT NOT NULL UNIQUE,
    Diagnosis NVARCHAR(255),
    ClinicalNote NVARCHAR(255),

    CONSTRAINT FK_MedicalRecords_Booking
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID)
);

-- =========================
-- 12. Prescription
-- =========================
CREATE TABLE Prescription (
    PrescriptionID INT IDENTITY(1,1) PRIMARY KEY,
    MedicalRecordID INT NOT NULL,
    Note NVARCHAR(255),

    CONSTRAINT FK_Prescription_MedicalRecords
    FOREIGN KEY (MedicalRecordID) REFERENCES MedicalRecords(MedicalRecordID)
);

-- =========================
-- 13. Drug
-- =========================
CREATE TABLE Drug (
    DrugID INT IDENTITY(1,1) PRIMARY KEY,
    Price DECIMAL(10,2),
    Description NVARCHAR(255),
    Unit NVARCHAR(50), -- Pill, Bottle, ml
    Name NVARCHAR(150),
    IsActive BIT DEFAULT 1
);

-- =========================
-- 14. Prescription_Drug (Junction Table)
-- =========================
CREATE TABLE Prescription_Drug (
    DrugID INT NOT NULL,
    PrescriptionID INT NOT NULL,
    Quantity INT,
    Instruction NVARCHAR(255),

    CONSTRAINT PK_Prescription_Drug
    PRIMARY KEY (DrugID, PrescriptionID),

    CONSTRAINT FK_PD_Drug
    FOREIGN KEY (DrugID) REFERENCES Drug(DrugID),

    CONSTRAINT FK_PD_Prescription
    FOREIGN KEY (PrescriptionID) REFERENCES Prescription(PrescriptionID)
);

-- =========================
-- 15. TestOrders
-- =========================
CREATE TABLE TestOrders (
    TestOrderID INT IDENTITY(1,1) PRIMARY KEY,
    MedicalRecordID INT NOT NULL,
    OrderType NVARCHAR(100), -- X-Ray, Blood Test
    OrderDateTime DATETIME,
    Instruction NVARCHAR(255),
    Status NVARCHAR(50), -- Pending, Completed
    StaffID INT NOT NULL, -- Technician performing test

    CONSTRAINT FK_TestOrders_MedicalRecords
    FOREIGN KEY (MedicalRecordID) REFERENCES MedicalRecords(MedicalRecordID),

    CONSTRAINT FK_TestOrders_Staff
    FOREIGN KEY (StaffID) REFERENCES Staff(StaffID)
);

-- =========================
-- 16. Invoices
-- =========================
CREATE TABLE Invoices (
    InvoiceID INT IDENTITY(1,1) PRIMARY KEY,
    BookingID INT NOT NULL UNIQUE,
    TotalAmount DECIMAL(12,2),
    PaymentStatus NVARCHAR(50), -- Unpaid, Paid

    CONSTRAINT FK_Invoices_Booking
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID)
);

-- =========================
-- 17. Payments
-- =========================
CREATE TABLE Payments (
    PaymentID INT IDENTITY(1,1) PRIMARY KEY,
    InvoiceID INT NOT NULL,
    TotalAmount DECIMAL(12,2),
    Method NVARCHAR(50), -- Cash, Credit Card
    Status NVARCHAR(50), -- Successful, Failed
    DateTime DATETIME DEFAULT GETDATE(),

    CONSTRAINT FK_Payments_Invoices
    FOREIGN KEY (InvoiceID) REFERENCES Invoices(InvoiceID)
);

-- =========================
-- 18. CareTasks
-- =========================
CREATE TABLE CareTasks (
    CareTaskID INT IDENTITY(1,1) PRIMARY KEY,
    TaskName NVARCHAR(150),
    Description NVARCHAR(255),
    StaffID INT NOT NULL,

    CONSTRAINT FK_CareTasks_Staff
    FOREIGN KEY (StaffID) REFERENCES Staff(StaffID)
);

-- =========================
-- 19. CareJourners (Note: Keeping original name, usually spelled Journals)
-- =========================
CREATE TABLE CareJourners (
    CareJID INT IDENTITY(1,1) PRIMARY KEY,
    CatID INT NOT NULL,
    Note NVARCHAR(255),
    RecordTime DATETIME DEFAULT GETDATE(),
    CareStaffID INT NOT NULL,

    CONSTRAINT FK_CareJourners_Cats
    FOREIGN KEY (CatID) REFERENCES Cats(CatID),

    CONSTRAINT FK_CareJourners_Staff
    FOREIGN KEY (CareStaffID) REFERENCES Staff(StaffID)
);

-- =============================================================
-- DATA SEEDING (ENGLISH)
-- =============================================================

-- 1. Insert Roles
INSERT INTO Roles (RoleName) VALUES 
('Manager'), 
('Veterinarian'), 
('Staff'), 
('Customer');

-- 2. Insert Users (Password is '123')
-- Admin/Manager (Male=1)
INSERT INTO Users (UserName, Password, FullName, Male, Email, RoleID, Phone) 
VALUES ('admin', '123', 'System Administrator', 1, 'admin@petclinic.com', 1, '123-456-7890');

-- Veterinarian (Male=1)
INSERT INTO Users (UserName, Password, FullName, Male, Email, RoleID, Phone) 
VALUES ('dr_smith', '123', 'Dr. John Smith', 1, 'smith@petclinic.com', 2, '123-555-0101');

-- Staff/Technician (Female=0)
INSERT INTO Users (UserName, Password, FullName, Male, Email, RoleID, Phone) 
VALUES ('nurse_mary', '123', 'Mary Johnson', 0, 'mary@petclinic.com', 3, '123-555-0102');

-- Owner/Customer (Female=0)
INSERT INTO Users (UserName, Password, FullName, Male, Email, RoleID, Phone) 
VALUES ('lisa_owner', '123', 'Lisa Wong', 0, 'lisa@gmail.com', 4, '987-654-3210');

-- 3. Insert specific Role tables
INSERT INTO Staff (UserID, Position) VALUES (1, 'Clinic Manager');
INSERT INTO Staff (UserID, Position) VALUES (3, 'Veterinary Technician');

INSERT INTO Veterinarians (UserID, Degree, ExperienceYear) VALUES (2, 'Doctor of Veterinary Medicine (DVM)', 10);

INSERT INTO Owners (UserID, Address) VALUES (4, '456 Oak Avenue, New York, NY');

-- 4. Insert Cats (Gender: 1=Male, 0=Female)
INSERT INTO Cats (OwnerID, Name, Gender, Breed, Age) 
VALUES 
(1, 'Luna', 0, 'Siamese', 2),
(1, 'Milo', 1, 'American Shorthair', 4),
(1, 'Bella', 0, 'Persian', 3),
(1, 'Leo', 1, 'Maine Coon', 5),
(1, 'Lucy', 0, 'British Shorthair', 1),
(1, 'Simba', 1, 'Bengal', 2),
(1, 'Nala', 0, 'Ragdoll', 3),
(1, 'Loki', 1, 'Sphynx', 4),
(1, 'Chloe', 0, 'Russian Blue', 2),
(1, 'Oreo', 1, 'Tuxedo', 3),
(1, 'Max', 1, 'Scottish Fold', 5),
(1, 'Sophie', 0, 'Siberian', 1),
(1, 'Charlie', 1, 'Abyssinian', 2),
(1, 'Daisy', 0, 'Birman', 4),
(1, 'Jack', 1, 'Norwegian Forest', 6),
(1, 'Lily', 0, 'Himalayan', 3),
(1, 'Jasper', 1, 'Devon Rex', 2),
(1, 'Cleo', 0, 'Egyptian Mau', 5),
(1, 'Oliver', 1, 'Munchkin', 1),
(1, 'Coco', 0, 'Burmese', 4);

INSERT INTO Cats (OwnerID, Name, Gender, Breed, Age) 
VALUES (1, 'Luna', 0, 'Siamese', 2);

-- 5. Insert Services
INSERT INTO Service (NameService, Price, Description, TimeService, IsActive) VALUES 
('General Checkup', 50.00, 'Routine health examination', 30, 1),
('Vaccination', 30.00, 'Annual rabies and distemper vaccines', 15, 1),
('Surgery', 500.00, 'Major surgical procedure', 120, 1);

-- 6. Insert Drugs
INSERT INTO Drug (Name, Price, Unit, Description, IsActive) VALUES 
('Amoxicillin', 15.00, 'Tablet', 'Antibiotic for infections', 1),
('Meloxicam', 20.00, 'ml', 'Pain reliever and anti-inflammatory', 1);

-- 7. Insert TimeSlots (For Vet ID 1)
INSERT INTO TimeSlots (VeterinarianID, StartTime, EndTime, Date, Status) 
VALUES (1, '09:00:00', '10:00:00', '2025-10-01', 'Available');

INSERT INTO TimeSlots (VeterinarianID, StartTime, EndTime, Date, Status) 
VALUES (1, '10:00:00', '11:00:00', '2025-10-01', 'Booked');

-- 8. Insert Booking
INSERT INTO Booking (CatID, VeterinarianID, Status, AppointmentDate) 
VALUES (1, 1, 'Completed', '2025-09-15 10:00:00');

-- 9. Insert Appointment Services
INSERT INTO Appointment_Service (BookingID, ServiceID) VALUES (1, 1); -- Checkup

-- 10. Insert Medical Record
INSERT INTO MedicalRecords (BookingID, Diagnosis, ClinicalNote) 
VALUES (1, 'Mild Gastritis', 'Patient is eating well but vomiting occasionally. Recommended diet change.');

-- 11. Insert Prescription
INSERT INTO Prescription (MedicalRecordID, Note) VALUES (1, 'Take with food.');

-- 12. Insert Prescription Drug
INSERT INTO Prescription_Drug (DrugID, PrescriptionID, Quantity, Instruction) 
VALUES (1, 1, 10, '1 tablet every 12 hours for 5 days');

-- 13. Insert Test Orders
INSERT INTO TestOrders (MedicalRecordID, OrderType, OrderDateTime, Instruction, Status, StaffID) 
VALUES (1, 'Blood Test', '2025-09-15 10:30:00', 'Check white blood cell count', 'Completed', 2); -- StaffID 2 is Nurse Mary

-- 14. Insert Invoice
INSERT INTO Invoices (BookingID, TotalAmount, PaymentStatus) 
VALUES (1, 65.00, 'Paid'); -- 50 (Service) + 15 (Drug)

-- 15. Insert Payment
INSERT INTO Payments (InvoiceID, TotalAmount, Method, Status, DateTime) 
VALUES (1, 65.00, 'Credit Card', 'Success', '2025-09-15 11:00:00');

-- 16. Insert CareTasks
INSERT INTO CareTasks (TaskName, Description, StaffID) 
VALUES ('Morning Feeding', 'Feed all boarding cats', 2);

-- 17. Insert CareJourners
INSERT INTO CareJourners (CatID, Note, RecordTime, CareStaffID) 
VALUES (1, 'Cat ate all food and looks happy.', GETDATE(), 2);

PRINT 'Database recreated successfully with English data and BIT gender fields.';