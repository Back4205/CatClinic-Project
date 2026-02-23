-- =============================================
-- THÊM SLOT MẪU ĐỂ TEST BOOKING
-- Chạy script này trên DB Cat_Clinic_Final để có slot trống cho bác sĩ
-- =============================================
USE Cat_Clinic_Final;
GO

-- Thêm slot trống cho VetID 1 (Dr. John Smith) - 7 ngày từ hôm nay
-- Mỗi ngày có 3 slot: 09:00, 11:00, 14:00
DECLARE @i INT = 0;
WHILE @i < 7
BEGIN
    INSERT INTO TimeSlots (VetID, SlotDate, StartTime, EndTime, Status) VALUES 
    (1, DATEADD(DAY, @i, CAST(GETDATE() AS DATE)), '09:00', '10:00', 'Available'),
    (1, DATEADD(DAY, @i, CAST(GETDATE() AS DATE)), '11:00', '12:00', 'Available'),
    (1, DATEADD(DAY, @i, CAST(GETDATE() AS DATE)), '14:00', '15:00', 'Available');
    SET @i = @i + 1;
END

PRINT 'Đã thêm slot mẫu thành công.';

-- Kiểm tra: chạy query này để xem slot đã có chưa
-- SELECT * FROM TimeSlots WHERE VetID = 1 AND Status = 'Available' ORDER BY SlotDate, StartTime;
