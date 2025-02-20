package com.laklu.pos.dataObjects.request;

import com.laklu.pos.enums.ShiftType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class NewSchedule {
    private Integer staffId; // ID nhân viên
    private LocalDate shiftDate; // Ngày làm việc
    private LocalTime shiftStart; // Giờ bắt đầu ca
    private LocalTime shiftEnd; // Giờ kết thúc ca
    private ShiftType shiftType; // Loại ca
    private String note;
}
