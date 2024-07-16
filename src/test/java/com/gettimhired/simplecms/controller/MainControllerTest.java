package com.gettimhired.simplecms.controller;

import com.gettimhired.simplecms.model.dto.ContactFormDto;
import com.gettimhired.simplecms.model.dto.JobDTO;
import com.gettimhired.simplecms.model.dto.JobFormDTO;
import com.gettimhired.simplecms.model.dto.MainPageDTO;
import com.gettimhired.simplecms.model.mongo.ContactStatus;
import com.gettimhired.simplecms.service.ContactService;
import com.gettimhired.simplecms.service.JobService;
import com.gettimhired.simplecms.service.MainPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MainControllerTest {

    @Mock
    private ContactService contactService;

    @Mock
    private JobService jobService;

    @Mock
    private MainPageService mainPageService;

    @InjectMocks
    private MainController mainController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }

    @Test
    public void testIndex() throws Exception {
        MainPageDTO mainPageDTO = new MainPageDTO("id","Title", true, "Section One", "Content", "Section Two", "Content", "Section Three", "Content", "Section Four", "Content");
        when(mainPageService.findMainPage()).thenReturn(Optional.of(mainPageDTO));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("mainPage"));

        verify(mainPageService, times(1)).findMainPage();
    }

    @Test
    public void testGallery() throws Exception {
        mockMvc.perform(get("/gallery"))
                .andExpect(status().isOk())
                .andExpect(view().name("gallerys"))
                .andExpect(model().attributeExists("jobs"));

        verify(jobService, times(1)).findAllJobs();
    }

    @Test
    public void testJob() throws Exception {
        JobDTO jobDTO = new JobDTO("1", "Job Title", true, true, true, true, true, "Job Description", Instant.now().getEpochSecond());
        when(jobService.findJobById("1")).thenReturn(Optional.of(jobDTO));

        mockMvc.perform(get("/job/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("jobs"))
                .andExpect(model().attributeExists("job"));

        verify(jobService, times(1)).findJobById("1");
    }

    @Test
    public void testContact() throws Exception {
        MainPageDTO mainPageDTO = new MainPageDTO("id","Title", true, "Section One", "Content", "Section Two", "Content", "Section Three", "Content", "Section Four", "Content");
        when(mainPageService.findMainPage()).thenReturn(Optional.of(mainPageDTO));

        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts"))
                .andExpect(model().attributeExists("contactFormDto"))
                .andExpect(model().attributeExists("title"));

        verify(mainPageService, times(1)).findMainPage();
    }

    @Test
    public void testCreateContact() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("name", "Test Name")
                        .param("email", "test@example.com")
                        .param("phoneNumber", "1234567890")
                        .param("message", "Test message"))
                .andExpect(status().isOk())
                .andExpect(view().name("contacts"))
                .andExpect(model().attributeExists("contactSaved"));

        verify(contactService, times(1)).save(any(ContactFormDto.class));
    }

    @Test
    public void testAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admins"))
                .andExpect(model().attributeExists("contacts"))
                .andExpect(model().attributeExists("jobs"));

        verify(contactService, times(1)).findAllContacts();
        verify(jobService, times(1)).findAllJobs();
    }

    @Test
    public void testLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("logins"));
    }

    @Test
    public void testNewJob() throws Exception {
        mockMvc.perform(get("/job/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("newjob"))
                .andExpect(model().attributeExists("jobFormDto"));
    }

    @Test
    public void testCreateJob() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("mainImage", "test.jpeg", "text/plain", "Test file content".getBytes());
        MockMultipartFile mockMultipartFileSub1 = new MockMultipartFile("subImage1", "test.jpeg", "text/plain", "Test file content".getBytes());
        MockMultipartFile mockMultipartFileSub2 = new MockMultipartFile("subImage2", "test.jpeg", "text/plain", "Test file content".getBytes());
        MockMultipartFile mockMultipartFileSub3 = new MockMultipartFile("subImage3", "test.jpeg", "text/plain", "Test file content".getBytes());
        MockMultipartFile mockMultipartFileSub4 = new MockMultipartFile("subImage4", "test.jpeg", "text/plain", "Test file content".getBytes());
        mockMvc.perform(multipart("/job/new")
                        .file(mockMultipartFile)
                        .file(mockMultipartFileSub1)
                        .file(mockMultipartFileSub2)
                        .file(mockMultipartFileSub3)
                        .file(mockMultipartFileSub4)
                        .param("title", "Test Job")
                        .param("description", "Test description")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("newjob"))
                .andExpect(model().attributeExists("jobSaved"));

        verify(jobService, times(1)).save(any(JobFormDTO.class));
    }

    @Test
    public void testEditJobPage() throws Exception {
        JobDTO jobDTO = new JobDTO("1", "Job Title", true, true, true, true, true, "Job Description", Instant.now().getEpochSecond());
        when(jobService.findJobById("1")).thenReturn(Optional.of(jobDTO));

        mockMvc.perform(get("/job/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("editjob"))
                .andExpect(model().attributeExists("jobEditDto"));

        verify(jobService, times(1)).findJobById("1");
    }

    @Test
    public void testEditJob() throws Exception {
        mockMvc.perform(post("/job/1/edit")
                        .param("title", "Updated Job Title")
                        .param("description", "Updated description"))
                .andExpect(status().isOk())
                .andExpect(view().name("editjob"))
                .andExpect(model().attributeExists("jobSaved"));

        verify(jobService, times(1)).updateJob(eq("1"), any());
    }

    @Test
    public void testDeleteJob() throws Exception {
        mockMvc.perform(post("/job/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(jobService, times(1)).deleteJobById("1");
    }

    @Test
    public void testUpdateContact() throws Exception {
        mockMvc.perform(post("/contact/1")
                        .param("status", ContactStatus.READ.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(contactService, times(1)).updateContactStatus(eq("1"), eq(ContactStatus.READ));
    }

    @Test
    public void testGetMainPage() throws Exception {
        MainPageDTO mainPageDTO = new MainPageDTO("id","Title", true, "Section One", "Content", "Section Two", "Content", "Section Three", "Content", "Section Four", "Content");
        when(mainPageService.findMainPage()).thenReturn(Optional.of(mainPageDTO));

        mockMvc.perform(get("/main-page/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("editmain"))
                .andExpect(model().attributeExists("mainPageEdit"));

        verify(mainPageService, times(1)).findMainPage();
    }

    @Test
    public void testEditMainPage() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("mainPageImage", "test.jpeg", "text/plain", "Test file content".getBytes());
        mockMvc.perform(multipart("/main-page/edit")
                        .file(mockMultipartFile)
                        .param("hasMainPageImage", "true")
                        .param("deleteMainPageImage", "false")
                        .param("title", "Updated Title")
                        .param("sectionOneTitle", "Updated Section One Title")
                        .param("sectionOneContent", "Updated Section One Content")
                        .param("sectionTwoTitle", "Updated Section One Title")
                        .param("sectionTwoContent", "Updated Section One Content")
                        .param("sectionThreeTitle", "Updated Section One Title")
                        .param("sectionThreeContent", "Updated Section One Content")
                        .param("sectionFourTitle", "Updated Section One Title")
                        .param("sectionFourContent", "Updated Section One Content")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("editmain"))
                .andExpect(model().attributeExists("mainPageSaved"));

        verify(mainPageService, times(1)).update(any());
    }
}