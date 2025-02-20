package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.NewSchedule;
import com.laklu.pos.entities.Schedule;
import com.laklu.pos.entities.User;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.ScheduleRepository;
import com.laklu.pos.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;

    // Lấy danh sách tất cả lịch làm việc
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // Tìm lịch làm việc theo ID
    public Optional<Schedule> findScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    // Lấy lịch làm việc theo ID hoặc ném lỗi nếu không tìm thấy
    public Schedule findOrFail(Long id) {
        return this.findScheduleById(id).orElseThrow(NotFoundException::new);
    }

    // Lưu lịch làm việc mới
    public Schedule storeSchedule(NewSchedule newSchedule) {
        // Lấy thông tin nhân viên từ ID
        User staff = userService.findOrFail(newSchedule.getStaffId());
        Schedule schedule = new Schedule();
        schedule.setShiftDate(newSchedule.getShiftDate());
        schedule.setShiftStart(newSchedule.getShiftStart());
        schedule.setShiftEnd(newSchedule.getShiftEnd());
        schedule.setShiftType(newSchedule.getShiftType());
        schedule.setNote(newSchedule.getNote());
        schedule.setStaff(staff); // Gán nhân viên vào lịch làm việc

        return scheduleRepository.save(schedule);
    }

    // Cập nhật lịch làm việc (chưa hoàn thiện, cần logic xử lý cập nhật phần tử cụ thể)
    public Schedule editSchedule(Long id, NewSchedule newSchedule) {
        Schedule existingSchedule = this.findOrFail(id);

        // Cập nhật thông tin lịch làm việc
        existingSchedule.setShiftDate(newSchedule.getShiftDate());
        existingSchedule.setShiftStart(newSchedule.getShiftStart());
        existingSchedule.setShiftEnd(newSchedule.getShiftEnd());
        existingSchedule.setShiftType(newSchedule.getShiftType());
        existingSchedule.setNote(newSchedule.getNote());

        return scheduleRepository.save(existingSchedule);
    }

    // Xóa lịch làm việc
    public void deleteSchedule(Long id) {
        Schedule schedule = this.findOrFail(id);
        scheduleRepository.delete(schedule);
    }

}
