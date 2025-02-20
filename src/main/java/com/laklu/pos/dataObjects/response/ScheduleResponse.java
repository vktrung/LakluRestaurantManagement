package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.Schedule;
import com.laklu.pos.enums.ShiftType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ScheduleResponse {

    private Long id;
    private Integer staffId;
    private LocalDate shiftDate;
    private LocalTime shiftStart;
    private LocalTime shiftEnd;
    private ShiftType shiftType;
    private String note;

    public ScheduleResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.staffId = schedule.getStaff().getId();
        this.shiftDate = schedule.getShiftDate();
        this.shiftStart = schedule.getShiftStart();
        this.shiftEnd = schedule.getShiftEnd();
        this.shiftType = schedule.getShiftType();
        this.note = schedule.getNote();
    }
}