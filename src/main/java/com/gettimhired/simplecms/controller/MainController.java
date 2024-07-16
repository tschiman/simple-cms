package com.gettimhired.simplecms.controller;

import com.gettimhired.simplecms.model.dto.*;
import com.gettimhired.simplecms.model.mongo.ContactStatus;
import com.gettimhired.simplecms.service.ContactService;
import com.gettimhired.simplecms.service.JobService;
import com.gettimhired.simplecms.service.MainPageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class MainController {

    Logger log = LoggerFactory.getLogger(MainController.class);
    private final ContactService contactService;
    private final JobService jobService;
    private final MainPageService mainPageService;

    public MainController(ContactService contactService, JobService jobService, MainPageService mainPageService) {
        this.contactService = contactService;
        this.jobService = jobService;
        this.mainPageService = mainPageService;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("GET / index");
        var mainPage = mainPageService.findMainPage();
        mainPage.ifPresent(mainPageDTO -> model.addAttribute("mainPage", mainPageDTO));
        return "index";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        log.info("GET /gallery gallery");
        model.addAttribute("jobs", jobService.findAllJobs());
        return "gallerys";
    }

    @GetMapping("/job/{id}")
    public String jobs(@PathVariable String id, Model model) {
        log.info("GET /job/{id} jobs jobId={}", id);
        var jobOpt = jobService.findJobById(id);
        jobOpt.ifPresent((jobDTO -> model.addAttribute("job", jobDTO)));
        return "jobs";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        log.info("GET /contact contact");
        model.addAttribute("contactFormDto", new ContactFormDto(null, null, null, null));
        model.addAttribute("title", mainPageService.findMainPage().get().title());
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
    public String admin(Model model, @RequestParam(defaultValue = "false") boolean showDeletedContacts) {
        log.info("GET /admin admin");

        var contactDtos = contactService.findAllContacts();
        List<ContactDTO> contactDtosNewList = new ArrayList<>(contactDtos);
        contactDtosNewList.sort(Comparator.comparingInt(c -> c.contactStatus().ordinal()));
        if (!showDeletedContacts) {
            contactDtosNewList = contactDtosNewList.stream()
                    .filter(contactDTO -> contactDTO.contactStatus() != ContactStatus.DELETED)
                    .toList();
        }
        var jobDtos = jobService.findAllJobs();

        model.addAttribute("contacts", contactDtosNewList);
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

    @PostMapping("/job/{id}/delete")
    public String editJob(@PathVariable String id) {
        log.info("POST /job/{id}/delete jobId={}", id);

        jobService.deleteJobById(id);

        return "redirect:/admin";
    }

    @PostMapping("/contact/{id}")
    public String updateContact(@PathVariable String id, @RequestParam ContactStatus status) {
        log.info("POST /contact/{id} contactId={}", id);
        contactService.updateContactStatus(id, status);
        return "redirect:/admin";
    }

    @GetMapping("/main-page/edit")
    public String getMainPage(Model model) {
        var mainPageDtoOpt = mainPageService.findMainPage();
        mainPageDtoOpt.ifPresent(mainPageDto -> {
            var mainPageEditDto = new MainPageEditDTO(
                    mainPageDto.title(),
                    mainPageDto.hasMainPageImage(),
                    null,
                    false,
                    mainPageDto.sectionOneTitle(),
                    mainPageDto.sectionOneContent(),
                    mainPageDto.sectionTwoTitle(),
                    mainPageDto.sectionTwoContent(),
                    mainPageDto.sectionThreeTitle(),
                    mainPageDto.sectionThreeContent()
            );
            model.addAttribute("mainPageEdit", mainPageEditDto);
        });
        return "editmain";
    }

    @PostMapping("/main-page/edit")
    public String editMainPage(@Valid @ModelAttribute MainPageEditDTO mainPageEditDto, BindingResult bindingResult, Model model) {

        checkImageFileType(mainPageEditDto.mainPageImage(), bindingResult, "mainPageImage");

        if (bindingResult.hasErrors()) {
            var mainPageDtoOpt = mainPageService.findMainPage();
            mainPageDtoOpt.ifPresent(mainPageDto -> {
                var mainPageEditDtoNew = new MainPageEditDTO(
                        mainPageDto.title(),
                        mainPageDto.hasMainPageImage(),
                        null,
                        false,
                        mainPageDto.sectionOneTitle(),
                        mainPageDto.sectionOneContent(),
                        mainPageDto.sectionTwoTitle(),
                        mainPageDto.sectionTwoContent(),
                        mainPageDto.sectionThreeTitle(),
                        mainPageDto.sectionThreeContent()
                );
                model.addAttribute("mainPageEdit", mainPageEditDtoNew);
            });
            return "editmain";
        }

        mainPageService.update(mainPageEditDto);
        model.addAttribute("mainPageSaved", true);

        return "editmain";
    }

    private void checkImageFileType(MultipartFile multipartFile, BindingResult bindingResult, String fieldName) {
        if(multipartFile != null && multipartFile.getOriginalFilename() != null && !multipartFile.getOriginalFilename().isEmpty()) {
            var notJpegAndNotPng = !multipartFile.getOriginalFilename().endsWith(".jpeg") && !multipartFile.getOriginalFilename().endsWith(".png") && !multipartFile.getOriginalFilename().endsWith(".jpg");
            if (notJpegAndNotPng) {
                log.info("Image has error fieldName={}", fieldName);
                bindingResult.addError(new FieldError("jobFormDto",fieldName, "Images must be jpeg or png file types"));
            }
        }
    }
}
