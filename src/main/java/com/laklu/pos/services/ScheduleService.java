package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.NewSchedule;
import com.laklu.pos.entities.Schedule;
import com.laklu.pos.entities.User;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.mapper.ScheduleMapper;
import com.laklu.pos.repositories.ScheduleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserService userService;
    private final ScheduleMapper scheduleMapper;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> findScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule findOrFail(Long id) {
        return this.findScheduleById(id).orElseThrow(NotFoundException::new);
    }

    public Schedule storeSchedule(NewSchedule newSchedule) {
        User staff = userService.findOrFail(newSchedule.getStaffId());
        Schedule schedule = scheduleMapper.toSchedule(newSchedule);
        schedule.setStaff(staff);

        return scheduleRepository.save(schedule);
    }

    public Schedule editSchedule(Long id, NewSchedule newSchedule) {
        Schedule existingSchedule = this.findOrFail(id);
        scheduleMapper.updateScheduleFromDto(newSchedule, existingSchedule);

        return scheduleRepository.save(existingSchedule);
    }

    public void deleteSchedule(Long id) {
        Schedule schedule = this.findOrFail(id);
        scheduleRepository.delete(schedule);
    }
}
