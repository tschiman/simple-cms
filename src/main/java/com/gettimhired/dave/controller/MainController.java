package com.gettimhired.dave.controller;

import com.gettimhired.dave.model.dto.ContactFormDto;
import com.gettimhired.dave.model.dto.JobFormDTO;
import com.gettimhired.dave.service.ContactService;
import com.gettimhired.dave.service.JobService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    Logger log = LoggerFactory.getLogger(MainController.class);
    private final ContactService contactService;
    private final JobService jobService;

    public MainController(ContactService contactService, JobService jobService) {
        this.contactService = contactService;
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String index() {
        log.info("GET / index");
        return "index";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        log.info("GET /gallery gallery");
        return "gallerys";
    }

    @GetMapping("/jobs/{id}")
    public String jobs(@PathVariable String id, Model model) {
        log.info("GET /jobs/{id} gallery jobId={}", id);
        return "jobs";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        log.info("GET /contact contact");
        model.addAttribute("contactFormDto", new ContactFormDto(null, null, null, null));
        return "contacts";
    }

    @PostMapping("/contact")
    public String createContact(@Valid @ModelAttribute ContactFormDto contactFormDto, BindingResult bindingResult, Model model) {
        log.info("POST /contact createContact");

        if (bindingResult.hasErrors()) {
            return "contacts";
        }

        contactService.save(contactFormDto);

        model.addAttribute("contactSaved", true);
        return "contacts";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        log.info("GET /admin admin");
        var contactDtos = contactService.findAllContacts();
        var jobDtos = jobService.findAllJobs();
        model.addAttribute("contacts", contactDtos);
        model.addAttribute("jobs", jobDtos);
        return "admins";
    }

    @GetMapping("/login")
    public String login() {
        log.info("GET /login login");
        return "logins";
    }

    @GetMapping("/job/new")
    public String newJob(Model model) {
        log.info("GET /job/new newJob");
        model.addAttribute("jobFormDto", new JobFormDTO(null, null, null));
        return "newjob";
    }

    @PostMapping("/job/new")
    public String createJob(@Valid @ModelAttribute JobFormDTO jobFormDto, BindingResult bindingResult, Model model) {
        log.info("POST /job/new createJob");

        if (jobFormDto.mainImage().isEmpty()) {
            bindingResult.addError(new ObjectError("mainImage", "An image is required"));
        }

        if(jobFormDto.mainImage().getOriginalFilename() != null) {
            var notJpegAndNotPng = !jobFormDto.mainImage().getOriginalFilename().endsWith(".jpeg") && !jobFormDto.mainImage().getOriginalFilename().endsWith(".png");
            if (notJpegAndNotPng) {
                bindingResult.addError(new FieldError("jobFormDto","mainImage", "Images must be jpeg or png file types"));
            }
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("jobFormDto", jobFormDto);
            return "newjob";
        }

        jobService.save(jobFormDto);
        model.addAttribute("jobSaved", true);

        return "newjob";
    }

    @GetMapping("/job/{id}/main-image/{imageId}")
    public ResponseEntity<byte[]> getMainImage(@PathVariable String id, @PathVariable String imageId) {
        var imageBytes = jobService.findJobMainImage(id, imageId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.IMAGE_JPEG); // Adjust based on the image type
        headers.setContentLength(imageBytes.length);
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
