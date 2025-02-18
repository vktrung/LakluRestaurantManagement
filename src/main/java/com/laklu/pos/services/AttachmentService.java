package com.laklu.pos.services;

import com.laklu.pos.entities.Attachment;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.AttachmentRepository;
import com.laklu.pos.validator.FileMustBeValid;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.TableMustAvailable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttachmentService {

    @Value("${app.base.url}")
    private String appBaseurl;
    @Value("${app.base.attachment-endpoint}")
    private String attachmentEndpoint;

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";

    AttachmentRepository attachmentRepository;

    public Attachment saveFile(MultipartFile file) throws IOException {
        RuleValidator.validate(new FileMustBeValid(file));

        String randomName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(randomName);
        file.transferTo(filePath.toFile());

        Attachment attachment = new Attachment();
        attachment.setMimeType(file.getContentType());
        attachment.setOriginalName(file.getOriginalFilename());
        attachment.setRandomName(randomName);
        attachment.setSize(file.getSize());
        attachment.setPath("/uploads/" + randomName);

        return attachmentRepository.save(attachment);
    }

    public String getImageUrl(String name) {
        Attachment attachment = attachmentRepository.findByRandomName(name).orElseThrow(NotFoundException::new);
        return appBaseurl + "/" + attachmentEndpoint + "/" + attachment.getPath();
    }

    public String getImageUrl(Long id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(NotFoundException::new);
        return appBaseurl + "/" + attachmentEndpoint + "/" + attachment.getPath();
    }

    public String getImageUrl(@NotNull Attachment attachment) {
        return appBaseurl + "/" + attachmentEndpoint + "/" + attachment.getPath();
    }

    public void assignTarget(String targetName, Long targetId, Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId).orElseThrow(NotFoundException::new);
        attachment.setTargetName(targetName);
        attachment.setTargetId(targetId);
        attachmentRepository.save(attachment);
    }

    public void cleanUp() {
       //TODO: Lấy ra toàn bộ ảnh trong database kiểm tra ảnh nào đang không có target id và targetName thì xóa.
    }



}
