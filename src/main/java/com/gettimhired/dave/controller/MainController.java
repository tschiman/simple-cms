package com.gettimhired.dave.controller;

import com.gettimhired.dave.model.dto.ContactFormDto;
import com.gettimhired.dave.model.dto.JobEditDTO;
import com.gettimhired.dave.model.dto.JobFormDTO;
import com.gettimhired.dave.service.ContactService;
import com.gettimhired.dave.service.JobService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

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
        model.addAttribute("jobFormDto", new JobFormDTO(null, null, null,null,null,null,null));
        return "newjob";
    }

    @PostMapping("/job/new")
    public String createJob(@Valid @ModelAttribute JobFormDTO jobFormDto, BindingResult bindingResult, Model model) {
        log.info("POST /job/new createJob");

        if (jobFormDto.mainImage().isEmpty()) {
            bindingResult.addError(new ObjectError("mainImage", "An image is required"));
        }

        checkImageFileType(jobFormDto.mainImage(), bindingResult, "mainImage");
        checkImageFileType(jobFormDto.subImage1(), bindingResult, "subImage1");
        checkImageFileType(jobFormDto.subImage2(), bindingResult, "subImage2");
        checkImageFileType(jobFormDto.subImage3(), bindingResult, "subImage3");
        checkImageFileType(jobFormDto.subImage4(), bindingResult, "subImage4");

        if (bindingResult.hasErrors()) {
            model.addAttribute("jobFormDto", jobFormDto);
            return "newjob";
        }

        jobService.save(jobFormDto);
        model.addAttribute("jobSaved", true);

        return "newjob";
    }

    @GetMapping("/job/{id}/edit")
    public String editJobPage(@PathVariable String id, Model model) {
        log.info("GET /job/{id}/edit jobId={}", id);
        var jobOpt = jobService.findJobById(id);
        jobOpt.ifPresent(jobDTO -> model.addAttribute(
                "jobEditDto",
                new JobEditDTO(
                        jobDTO.id(),
                        jobDTO.title(),
                        jobDTO.hasMainImage(),
                        jobDTO.hasSubImage1(),
                        null,
                        false,
                        jobDTO.hasSubImage2(),
                        null,
                        false,
                        jobDTO.hasSubImage3(),
                        false,
                        null,
                        jobDTO.hasSubImage4(),
                        false,
                        null,
                        jobDTO.description()
                )));
        return "editjob";
    }

    @PostMapping("/job/{id}/edit")
    public String editJob(@PathVariable String id, @Valid @ModelAttribute JobEditDTO jobEditDTO, BindingResult bindingResult, Model model) {
        log.info("POST /job/{id}/edit jobId={}", id);

        if (bindingResult.hasErrors()) {
            var jobOpt = jobService.findJobById(id);
            jobOpt.ifPresent(jobDTO -> model.addAttribute(
                    "jobEditDto",
                    new JobEditDTO(
                            jobDTO.id(),
                            jobDTO.title(),
                            jobDTO.hasMainImage(),
                            jobDTO.hasSubImage1(),
                            null,
                            false,
                            jobDTO.hasSubImage2(),
                            null,
                            false,
                            jobDTO.hasSubImage3(),
                            false,
                            null,
                            jobDTO.hasSubImage4(),
                            false,
                            null,
                            jobDTO.description()
                    )));
            return "editjob";
        }

        jobService.updateJob(id, jobEditDTO);

        model.addAttribute("jobSaved", true);
        model.addAttribute("jobId", id);
        return "editjob";
    }

    private void checkImageFileType(MultipartFile multipartFile, BindingResult bindingResult, String fieldName) {
        if(multipartFile.getOriginalFilename() != null && !multipartFile.getOriginalFilename().isEmpty()) {
            var notJpegAndNotPng = !multipartFile.getOriginalFilename().endsWith(".jpeg") && !multipartFile.getOriginalFilename().endsWith(".png");
            if (notJpegAndNotPng) {
                log.info("Image has error fieldName={}", fieldName);
                bindingResult.addError(new FieldError("jobFormDto",fieldName, "Images must be jpeg or png file types"));
            }
        }
    }
}
