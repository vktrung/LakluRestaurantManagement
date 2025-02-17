package com.laklu.pos.services;

import com.laklu.pos.entities.Attachment;
import com.laklu.pos.repositories.AttachmentRepository;
import com.laklu.pos.validator.FileMustBeValid;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.TableMustAvailable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AttachmentService {

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
}
