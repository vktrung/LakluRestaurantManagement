package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.Attachment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentResponse {
    private String mimeType;
    private String originalName;
    private String randomName;
    private Long size;
    private String targetName;
    private Long targetId;
    private String path;

    public AttachmentResponse(Attachment attachment) {
        this.mimeType = attachment.getMimeType();
        this.originalName = attachment.getOriginalName();
        this.randomName = attachment.getRandomName();
        this.size = attachment.getSize();
        this.targetName = attachment.getTargetName();
        this.targetId = attachment.getTargetId();
        this.path = attachment.getPath();
    }
}
