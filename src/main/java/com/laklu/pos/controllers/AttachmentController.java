package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.AttachmentPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.UploadFileRequest;
import com.laklu.pos.dataObjects.response.AttachmentResponse;
import com.laklu.pos.entities.Attachment;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.AttachmentService;
import com.laklu.pos.uiltis.Ultis;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Slf4j
@RestController
@RequestMapping("/api/v1/attachments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttachmentController {

    AttachmentService attachmentService;
    AttachmentPolicy attachmentPolicy;

    @PostMapping("/")
    public ApiResponseEntity store(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "targetName", required = false) String targetName,
            @RequestParam(value = "targetId", required = false) Long targetId) throws Exception {

        // Kiểm tra quyền của người dùng
        Ultis.throwUnless(attachmentPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        // Lưu file và trả về kết quả
        Attachment attachment = attachmentService.saveFile(file, targetName, targetId);

        return ApiResponseEntity.success(new AttachmentResponse(attachment));
    }
}
