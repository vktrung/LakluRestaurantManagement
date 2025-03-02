package com.laklu.pos.repositories;

import com.laklu.pos.entities.Attendance;
import com.laklu.pos.entities.Schedule;
import com.laklu.pos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {
    Optional<Attendance> findByScheduleAndStaff(Schedule schedule, User staff);
}
