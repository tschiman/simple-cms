package com.gettimhired.simplecms.controller;

import com.gettimhired.simplecms.service.JobService;
import com.gettimhired.simplecms.service.MainPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ImageController {
    Logger log = LoggerFactory.getLogger(ImageController.class);
    private final JobService jobService;
    private final MainPageService mainPageService;

    public ImageController(JobService jobService, MainPageService mainPageService) {
        this.jobService = jobService;
        this.mainPageService = mainPageService;
    }

    @GetMapping("/job/{id}/main-image")
    public ResponseEntity<byte[]> getMainImage(@PathVariable String id) {
        log.info("GET /job/{id}/main-image getMainImage jobId={}", id);
        var imageBytes = jobService.findJobMainImage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        if (imageBytes.length > 0) {
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/job/{id}/sub-image/{subImageId}")
    public ResponseEntity<byte[]> getSubImage(@PathVariable String id, @PathVariable String subImageId) {
        log.info("GET /job/{id}/sub-image/{subImageId} getSubImage jobId={} subImageId={}", id, subImageId);
        var imageBytes = jobService.findJobSubImage(id, subImageId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        if (imageBytes.length > 0) {
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/main-image")
    public ResponseEntity<byte[]> getMainImage() {
        log.info("GET /main-image getMainImage");
        var imageBytes = mainPageService.findMainImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.IMAGE_JPEG);
        headers.setContentLength(imageBytes.length);
        if (imageBytes.length > 0) {
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
