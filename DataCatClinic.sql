

CREATE DATABASE Cat_Clinic;
GO

USE Cat_Clinic;
GO


-- Lưu các vai trò trong hệ thống
CREATE TABLE Roles (
    RoleID INT IDENTITY PRIMARY KEY,        -- Khóa chính
    RoleName NVARCHAR(50) NOT NULL           -- Tên vai trò: Admin, Owner, Vet...
);

-- Tài khoản đăng nhập hệ thống (KHÔNG chứa nghiệp vụ)
CREATE TABLE Users (
    UserID INT IDENTITY PRIMARY KEY,         -- Khóa chính
    UserName NVARCHAR(50) NOT NULL,           -- Tên đăng nhập
    PassWord NVARCHAR(255) NOT NULL,          -- Mật khẩu (hash khi code)
    FullName NVARCHAR(100),                   -- Tên hiển thị
    Male BIT,                                 -- Giới tính (1: Nam, 0: Nữ)
    Email NVARCHAR(100),                      -- Email
    RoleID INT,                               -- Vai trò
    CONSTRAINT FK_Users_Roles
        FOREIGN KEY (RoleID) REFERENCES Roles(RoleID)
);
-- Nhân viên chăm sóc thú cưng (cho ăn, vệ sinh, theo dõi)
CREATE TABLE CareStaff (
    CareStaffID INT IDENTITY PRIMARY KEY,     -- Khóa chính
    UserID INT,                               -- Liên kết tài khoản
    FullName NVARCHAR(100),                   -- Họ tên
    Gender NVARCHAR(10),                      -- Giới tính (Male/Female)
    Email NVARCHAR(100),                      -- Email
    Phone NVARCHAR(20),                       -- SĐT
    CONSTRAINT FK_CareStaff_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Chủ nuôi thú cưng
CREATE TABLE Owners (
    OwnerID INT IDENTITY PRIMARY KEY,
    FullName NVARCHAR(100),
    Email NVARCHAR(100),
    Phone NVARCHAR(20),
    Address NVARCHAR(255),
    Male BIT,
    UserID INT,
    CONSTRAINT FK_Owners_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Thông tin mèo (thú cưng)
CREATE TABLE Cats (
    CatID INT IDENTITY PRIMARY KEY,
    OwnerID INT,                              -- Chủ nuôi
    Name NVARCHAR(100),                       -- Tên mèo
    Gender NVARCHAR(10),                      -- Giới tính
    Breed NVARCHAR(50),                       -- Giống
    Age INT,                                  -- Tuổi
    CONSTRAINT FK_Cats_Owners
        FOREIGN KEY (OwnerID) REFERENCES Owners(OwnerID)
);

-- Bác sĩ thú y
CREATE TABLE Veterinarians (
    VetID INT IDENTITY PRIMARY KEY,
    FullName NVARCHAR(100),
    Phone NVARCHAR(20),
    Email NVARCHAR(100),
    Degree NVARCHAR(100),                     -- Bằng cấp
    ExperienceYear INT,                       -- Số năm kinh nghiệm
    Male BIT,
    UserID INT,
    CONSTRAINT FK_Vets_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Kỹ thuật viên xét nghiệm
CREATE TABLE Technician (
    TechnicianID INT IDENTITY PRIMARY KEY,
    Name NVARCHAR(100),
    Age INT,
    Phone NVARCHAR(20),
    Certification NVARCHAR(100),
    Male BIT,
    UserID INT,
    CONSTRAINT FK_Technician_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);


-- Lễ tân
CREATE TABLE Receptionists (
    EmployeeID INT IDENTITY PRIMARY KEY,
    UserID INT,
    FullName NVARCHAR(100),
    Email NVARCHAR(100),
    Phone NVARCHAR(20),
    Gender NVARCHAR(10),
    CONSTRAINT FK_Receptionists_Users
        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
-- Dịch vụ phòng khám
CREATE TABLE Service (
    ServiceID INT IDENTITY PRIMARY KEY,
    NameService NVARCHAR(100),                -- Tên dịch vụ
    Price DECIMAL(10,2),                      -- Giá
    Description NVARCHAR(MAX),                -- Mô tả
    TimeService INT,                          -- Thời gian (phút)
    Status NVARCHAR(20),                      -- Active / Inactive
    CONSTRAINT CK_Service_Status
        CHECK (Status IN (N'Active', N'Inactive'))
);

-- Lịch hẹn khám
CREATE TABLE Booking (
    BookingID INT IDENTITY PRIMARY KEY,
    CatID INT,
    VeterinarianID INT,
    Status NVARCHAR(20),                      -- Paid / Unpaid
    AppointmentDate DATETIME,
    CONSTRAINT FK_Booking_Cats
        FOREIGN KEY (CatID) REFERENCES Cats(CatID),
    CONSTRAINT FK_Booking_Vets
        FOREIGN KEY (VeterinarianID) REFERENCES Veterinarians(VetID),
    CONSTRAINT CK_Booking_Status
        CHECK (Status IN (N'Unpaid', N'Paid'))
);

-- Dịch vụ được sử dụng trong một lịch hẹn
CREATE TABLE Appointment_service (
    BookingID INT,
    ServiceID INT,
    PRIMARY KEY (BookingID, ServiceID),
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID),
    FOREIGN KEY (ServiceID) REFERENCES Service(ServiceID)
);

-- Khung giờ làm việc của bác sĩ
CREATE TABLE TimeSlots (
    TimeSlotID INT IDENTITY PRIMARY KEY,
    VeterinarianID INT,
    StartTime TIME,
    EndTime TIME,
    [Date] DATE,
    Status NVARCHAR(20),                      -- Available / Booked
    FOREIGN KEY (VeterinarianID) REFERENCES Veterinarians(VetID)
);

-- Hồ sơ khám bệnh
CREATE TABLE MedicalRecords (
    MedicalRecordID INT IDENTITY PRIMARY KEY,
    BookingID INT,
    Diagnosis NVARCHAR(MAX),
    ClinicalNote NVARCHAR(MAX),
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID)
);
-- Đơn thuốc
CREATE TABLE Prescription (
    PrescriptionID INT IDENTITY PRIMARY KEY,
    MedicalRecordID INT,
    
    Instruction NVARCHAR(MAX),
    FOREIGN KEY (MedicalRecordID) REFERENCES MedicalRecords(MedicalRecordID)
);


-- Thuốc
CREATE TABLE Drug (
    DrugID INT IDENTITY PRIMARY KEY,
    Price DECIMAL(10,2),
    Description NVARCHAR(MAX),
    Unit NVARCHAR(50)
    
);

-- Chi tiết thuốc trong đơn
CREATE TABLE Prescription_Drug (
    DrugID INT,
    PrescriptionID INT,
	Quantity INT,
    PRIMARY KEY (DrugID, PrescriptionID),
    FOREIGN KEY (DrugID) REFERENCES Drug(DrugID),
    FOREIGN KEY (PrescriptionID) REFERENCES Prescription(PrescriptionID)
);

-- Yêu cầu xét nghiệm
CREATE TABLE TestOrders (
    TestOrderID INT IDENTITY PRIMARY KEY,
    MedicalRecordID INT,
    OrderType NVARCHAR(100),
    OrderDateTime DATETIME,
    Instruction NVARCHAR(MAX),
    Status NVARCHAR(50),
    TechnicianID INT,
    FOREIGN KEY (MedicalRecordID) REFERENCES MedicalRecords(MedicalRecordID),
    FOREIGN KEY (TechnicianID) REFERENCES Technician(TechnicianID)
);


-- Công việc chăm sóc
CREATE TABLE CareTasks (
    CareTaskID INT IDENTITY PRIMARY KEY,
    TaskName NVARCHAR(100),
    Description NVARCHAR(MAX)
);

-- Nhật ký chăm sóc
CREATE TABLE CareJourners (
    CareJID INT IDENTITY PRIMARY KEY,
    CatID INT,
    Note NVARCHAR(MAX),
    RecordTime DATETIME,
    CareStaffID INT,
    CareTaskID INT,
    FOREIGN KEY (CatID) REFERENCES Cats(CatID),
    FOREIGN KEY (CareTaskID) REFERENCES CareTasks(CareTaskID),
    FOREIGN KEY (CareStaffID) REFERENCES CareStaff(CareStaffID)
);
-- Hóa đơn
CREATE TABLE Invoices (
    InvoiceID INT IDENTITY PRIMARY KEY,
    BookingID INT,
    TotalAmount DECIMAL(10,2),
    PaymentStatus NVARCHAR(50),
    FOREIGN KEY (BookingID) REFERENCES Booking(BookingID)
);

-- Thanh toán
CREATE TABLE Payments (
    PaymentID INT IDENTITY PRIMARY KEY,
    InvoiceID INT,
    TotalAmount DECIMAL(10,2),
    Method NVARCHAR(50),
    Status NVARCHAR(50),
    DateTime DATETIME,
    FOREIGN KEY (InvoiceID) REFERENCES Invoices(InvoiceID)
);


INSERT INTO Roles (RoleName) VALUES
(N'Admin'),
(N'Owner'),
(N'Veterinarian'),
(N'Technician'),
(N'Receptionist'),
(N'CareStaff');


INSERT INTO Users (UserName, PassWord, FullName, Male, Email, RoleID) VALUES
(N'admin',   '123', N'Admin System',     1, 'admin@cat.com', 1),
(N'owner1',  '123', N'Nguyễn Văn A',      1, 'owner1@gmail.com', 2),
(N'vet1',    '123', N'Bác sĩ Trần B',     1, 'vet1@gmail.com',   3),
(N'tech1',   '123', N'Kỹ thuật Lê C',     1, 'tech1@gmail.com',  4),
(N'recep1',  '123', N'Lễ tân Phạm D',     0, 'reception@gmail.com', 5),
(N'care1',   '123', N'Nhân viên chăm sóc',0, 'care@gmail.com',  6);


INSERT INTO Owners (FullName, Email, Phone, Address, Male, UserID) VALUES
(N'Nguyễn Văn A', 'owner1@gmail.com', '0901111111', N'Hà Nội', 1, 2);

INSERT INTO Cats (OwnerID, Name, Gender, Breed, Age) VALUES
(1, N'Miu', N'Female', N'Anh lông ngắn', 2),
(1, N'Tom', N'Male',   N'Mèo ta',        3);

INSERT INTO Veterinarians
(FullName, Phone, Email, Degree, ExperienceYear, Male, UserID)
VALUES
(N'Bác sĩ Trần B', '0912222222', 'vet1@gmail.com', N'DVM', 5, 1, 3);

INSERT INTO Technician
(Name, Age, Phone, Certification, Male, UserID)
VALUES
(N'Kỹ thuật Lê C', 28, '0933333333', N'Lab Technician', 1, 4);


INSERT INTO Receptionists
(UserID, FullName, Email, Phone, Gender)
VALUES
(5, N'Lễ tân Phạm D', 'reception@gmail.com', '0944444444', N'Female');

INSERT INTO CareStaff
(UserID, FullName, Gender, Email, Phone)
VALUES
(6, N'Nhân viên chăm sóc 1', N'Female', 'care@gmail.com', '0955555555');

INSERT INTO Service (NameService, Price, Description, TimeService, Status) VALUES
(N'Khám chữa & điều trị', 300000, N'Dịch vụ khám và điều trị bệnh', 45, N'Active'),
(N'Khách sạn thú cưng',   150000, N'Dịch vụ lưu trú thú cưng',     1440, N'Active'),
(N'Phẫu thuật',          500000, N'Dịch vụ phẫu thuật thú cưng',   45, N'Active');


INSERT INTO TimeSlots
(VeterinarianID, StartTime, EndTime, [Date], Status)
VALUES
(1, '09:00', '10:00', '2026-01-24', N'Booked'),
(1, '10:00', '11:00', '2026-01-24', N'Available');

INSERT INTO Booking
(CatID, VeterinarianID, Status, AppointmentDate)
VALUES
(1, 1, N'Unpaid', '2026-01-24 09:00'),
(2, 1, N'Paid',   '2026-01-24 10:00');

INSERT INTO Appointment_service (BookingID, ServiceID) VALUES
(1, 1),
(2, 3);

INSERT INTO MedicalRecords
(BookingID, Diagnosis, ClinicalNote)
VALUES
(1, N'Sốt nhẹ', N'Theo dõi thêm'),
(2, N'Gãy chân', N'Cần phẫu thuật');


INSERT INTO Prescription
(MedicalRecordID, Instruction)
VALUES
(1, N'Uống sau ăn'),
(2, N'Uống sáng và tối');


INSERT INTO Drug
(Price, Description, Unit )
VALUES
(50000, N'Thuốc hạ sốt', N'Viên' ),
(80000, N'Thuốc kháng sinh', N'Viên');


INSERT INTO Prescription_Drug (DrugID, PrescriptionID,Quantity) VALUES
(1, 1,5),
(2, 2,10);

INSERT INTO TestOrders
(MedicalRecordID, OrderType, OrderDateTime, Instruction, Status, TechnicianID)
VALUES
(1, N'Xét nghiệm máu', '2026-01-24 09:30', N'Kiểm tra tổng quát', N'Pending', 1),
(2, N'X-Quang',       '2026-01-24 10:30', N'Chụp chân',         N'Completed', 1);


INSERT INTO CareTasks (TaskName, Description) VALUES
(N'Cho ăn', N'Cho thú cưng ăn'),
(N'Tắm rửa', N'Vệ sinh thú cưng');


INSERT INTO CareJourners
(CatID, Note, RecordTime, CareStaffID, CareTaskID)
VALUES
(1, N'Đã cho ăn', GETDATE(), 1, 1),
(2, N'Đã tắm',    GETDATE(), 1, 2);


INSERT INTO Invoices
(BookingID, TotalAmount, PaymentStatus)
VALUES
(1, 300000, N'Unpaid'),
(2, 500000, N'Paid');


INSERT INTO Payments
(InvoiceID, TotalAmount, Method, Status, DateTime)
VALUES
(2, 500000, N'Cash', N'Completed', GETDATE());




