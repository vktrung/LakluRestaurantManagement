package com.laklu.pos.services;

import com.google.zxing.WriterException;
import com.laklu.pos.dataObjects.ScheduleCheckInCode;
import com.laklu.pos.dataObjects.ScheduleCheckOutCode;
import com.laklu.pos.dataObjects.request.NewSchedule;
import com.laklu.pos.entities.Schedule;
import com.laklu.pos.entities.User;
import com.laklu.pos.exceptions.httpExceptions.BadRequestException;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.mapper.ScheduleMapper;
import com.laklu.pos.repositories.ScheduleRepository;
import com.laklu.pos.uiltis.Ultis;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.base.attendance-checkin}")
    private String checkInEndpoint;

    @Value("${app.base.attendance-checkout}")
    private String checkOutEndpoint;

    @Value("${app.base.attendance-expire-time}")
    private Long checkCodeExpiry;


    public ScheduleService(ScheduleRepository scheduleRepository, UserService userService, ScheduleMapper scheduleMapper, QRCodeGenerator qrCodeGenerator, SignedUrlGenerator signedUrlGenerator) {
        this.scheduleRepository = scheduleRepository;
        this.userService = userService;
        this.scheduleMapper = scheduleMapper;
        this.qrCodeGenerator = qrCodeGenerator;
        this.signedUrlGenerator = signedUrlGenerator;
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
        schedule.setStaff(staff);

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
}
