package com.laklu.pos.services;

import com.google.zxing.WriterException;
import com.laklu.pos.dataObjects.ScheduleCheckInCode;
import com.laklu.pos.dataObjects.ScheduleCheckOutCode;
import com.laklu.pos.dataObjects.request.NewSchedule;
import com.laklu.pos.entities.Attendance;
import com.laklu.pos.entities.Schedule;
import com.laklu.pos.entities.User;
import com.laklu.pos.exceptions.httpExceptions.BadRequestException;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.exceptions.httpExceptions.UnauthorizedException;
import com.laklu.pos.mapper.ScheduleMapper;
import com.laklu.pos.repositories.AttendanceRepository;
import com.laklu.pos.repositories.ScheduleRepository;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.valueObjects.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    private final UserService userService;

    private final ScheduleMapper scheduleMapper;

    private final QRCodeGenerator qrCodeGenerator;

    private final SignedUrlGenerator signedUrlGenerator;

    private final AuthenticationProvider daoAuthenticationProvider;

    private final AttendanceRepository attendanceRepository;

    @Value("${app.base.attendance-checkin}")
    private String checkInEndpoint;

    @Value("${app.base.attendance-checkout}")
    private String checkOutEndpoint;

    @Value("${app.base.attendance-expire-time}")
    private Long checkCodeExpiry;


    public ScheduleService(ScheduleRepository scheduleRepository, UserService userService, ScheduleMapper scheduleMapper, QRCodeGenerator qrCodeGenerator, SignedUrlGenerator signedUrlGenerator, AuthenticationProvider daoAuthenticationProvider, AttendanceRepository attendanceRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userService = userService;
        this.scheduleMapper = scheduleMapper;
        this.qrCodeGenerator = qrCodeGenerator;
        this.signedUrlGenerator = signedUrlGenerator;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.attendanceRepository = attendanceRepository;
    }

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
        schedule.addStaff(staff);

        return scheduleRepository.save(schedule);
    }

    public Schedule editSchedule(Schedule schedule, NewSchedule newSchedule) {
        scheduleMapper.updateScheduleFromDto(newSchedule, schedule);

        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id) {
        Schedule schedule = this.findOrFail(id);

        scheduleRepository.delete(schedule);
    }

    public byte[] generateCheckInCode(Schedule schedule) throws IOException, WriterException {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("scheduleId", schedule.getId().toString());
        String link = this.signedUrlGenerator.generateSignedUrl(checkInEndpoint, payload, checkCodeExpiry);
        ScheduleCheckInCode scheduleCheckInCode = new ScheduleCheckInCode(link);
        return this.qrCodeGenerator.getQRCode(scheduleCheckInCode, 200, 200);
    }

    public void validateCheckInCode(String scheduleId, long expiry, String signature) throws Exception {
        HashMap<String, String> signedUrlData = new HashMap<>();
        signedUrlData.put("scheduleId", scheduleId);
        Ultis.throwUnless(this.signedUrlGenerator.isGenneratedSignedUrl(signedUrlData, expiry, signature), new BadRequestException());
    }

    public byte[] generateCheckOutCode(Schedule schedule) throws IOException, WriterException {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("scheduleId", schedule.getId().toString());
        payload.put("checkOut", "true");
        String link = this.signedUrlGenerator.generateSignedUrl(checkOutEndpoint, payload, checkCodeExpiry);
        ScheduleCheckOutCode scheduleCheckInCode = new ScheduleCheckOutCode(link);
        return this.qrCodeGenerator.getQRCode(scheduleCheckInCode, 200, 200);
    }

    public void validateCheckOutCode(String scheduleId, long expiry, String signature) throws Exception {
        HashMap<String, String> signedUrlData = new HashMap<>();
        signedUrlData.put("scheduleId", scheduleId);
        signedUrlData.put("checkOut", "true");
        Ultis.throwUnless(this.signedUrlGenerator.isGenneratedSignedUrl(signedUrlData, expiry, signature), new BadRequestException());
    }

    public UserPrincipal getScheduleUser(String username, String password) throws Exception {
       var result = this.daoAuthenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(username, password));
       Ultis.throwUnless(result != null, new UnauthorizedException());

       return (UserPrincipal) result.getPrincipal();
    }

    public Attendance createCheckInAttendance(Schedule schedule, User user) {
        Attendance attendance = new Attendance();
        attendance.setSchedule(schedule);
        attendance.setStaff(user);
        attendance.setSchedule(schedule);
        attendance.setClockIn(Ultis.getCurrentTime());
        attendance.setStatus(Attendance.Status.PRESENT);

        this.attendanceRepository.save(attendance);
        return attendance;
    }

    public Attendance checkOutAttendance(Schedule schedule, User user) {
        Attendance attendance = this.attendanceRepository.findByScheduleAndStaff(schedule, user).orElseThrow(NotFoundException::new);
        attendance.setClockOut(Ultis.getCurrentTime());
        attendance.setStatus(Attendance.Status.PRESENT);
        this.attendanceRepository.save(attendance);
        return attendance;
    }

}
