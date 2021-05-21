package vn.prostylee.media.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.media.dto.request.MediaRequest;
import vn.prostylee.media.service.AttachmentService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping( ApiVersion.API_V1 + "/attachments")
public class AttachmentController {

    private AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping(value = "/store-file")
    public ResponseEntity<Integer> storeFile(@NotEmpty @RequestBody List<MediaRequest> mediaRequest) {
        return ResponseEntity.ok(attachmentService.storeFiles(mediaRequest));
    }
}
