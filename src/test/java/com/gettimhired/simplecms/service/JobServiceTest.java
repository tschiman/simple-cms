package com.gettimhired.simplecms.service;

import com.gettimhired.simplecms.model.dto.JobDTO;
import com.gettimhired.simplecms.model.dto.JobEditDTO;
import com.gettimhired.simplecms.model.dto.JobFormDTO;
import com.gettimhired.simplecms.model.mongo.Job;
import com.gettimhired.simplecms.repository.JobRepository;
import com.gettimhired.simplecms.util.GzipUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() throws Exception {
        MockMultipartFile mainImage = new MockMultipartFile("mainImage", "mainImage.png", "image/png", "Main image content".getBytes());
        MockMultipartFile subImage1 = new MockMultipartFile("subImage1", "subImage1.png", "image/png", "Sub image 1 content".getBytes());
        MockMultipartFile subImage2 = new MockMultipartFile("subImage2", "subImage2.png", "image/png", "Sub image 2 content".getBytes());
        MockMultipartFile subImage3 = new MockMultipartFile("subImage3", "subImage3.png", "image/png", "Sub image 3 content".getBytes());
        MockMultipartFile subImage4 = new MockMultipartFile("subImage4", "subImage4.png", "image/png", "Sub image 4 content".getBytes());

        JobFormDTO jobFormDTO = new JobFormDTO("Job Title", mainImage, subImage1, subImage2, subImage3, subImage4, "description");

        jobService.save(jobFormDTO);

        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    public void testFindAllJobs() {
        Job job1 = new Job(UUID.randomUUID().toString(), "Job Title 1", new byte[0], new byte[0], new byte[0], new byte[0], new byte[0], "Job Description 1", Instant.now().getEpochSecond());
        Job job2 = new Job(UUID.randomUUID().toString(), "Job Title 2", new byte[0], new byte[0], new byte[0], new byte[0], new byte[0], "Job Description 2", Instant.now().getEpochSecond());

        when(jobRepository.findAll()).thenReturn(List.of(job1, job2));

        List<JobDTO> jobs = jobService.findAllJobs();

        assertEquals(2, jobs.size());
        assertEquals(job1.id(), jobs.get(0).id());
        assertEquals(job2.id(), jobs.get(1).id());
        verify(jobRepository, times(1)).findAll();
    }

    @Test
    public void testFindJobMainImage() throws Exception {
        String jobId = UUID.randomUUID().toString();
        Job job = new Job(jobId, "Job Title", GzipUtil.compress("Main image content".getBytes()), new byte[0], new byte[0], new byte[0], new byte[0], "Job Description", Instant.now().getEpochSecond());

        when(jobRepository.findJobByIdReturnMainImage(jobId)).thenReturn(Optional.of(job));

        byte[] mainImage = jobService.findJobMainImage(jobId);

        assertArrayEquals("Main image content".getBytes(), mainImage);
        verify(jobRepository, times(1)).findJobByIdReturnMainImage(jobId);
    }

    @Test
    public void testFindJobMainImage_NotFound() {
        String jobId = UUID.randomUUID().toString();

        when(jobRepository.findJobByIdReturnMainImage(jobId)).thenReturn(Optional.empty());

        byte[] mainImage = jobService.findJobMainImage(jobId);

        assertArrayEquals(new byte[0], mainImage);
        verify(jobRepository, times(1)).findJobByIdReturnMainImage(jobId);
    }

    @Test
    public void testFindJobSubImage() throws Exception {
        String jobId = UUID.randomUUID().toString();
        Job job = new Job(jobId, "Job Title", new byte[0], GzipUtil.compress("Sub image 1 content".getBytes()), new byte[0], new byte[0], new byte[0], "Job Description", Instant.now().getEpochSecond());

        when(jobRepository.existsById(jobId)).thenReturn(true);
        when(jobRepository.findSubImage1(jobId)).thenReturn(job);

        byte[] subImage = jobService.findJobSubImage(jobId, "1");

        assertArrayEquals("Sub image 1 content".getBytes(), subImage);
        verify(jobRepository, times(1)).existsById(jobId);
        verify(jobRepository, times(1)).findSubImage1(jobId);
    }

    @Test
    public void testFindJobSubImage_NotFound() {
        String jobId = UUID.randomUUID().toString();

        when(jobRepository.existsById(jobId)).thenReturn(false);

        byte[] subImage = jobService.findJobSubImage(jobId, "1");

        assertArrayEquals(new byte[0], subImage);
        verify(jobRepository, times(1)).existsById(jobId);
    }

    @Test
    public void testFindJobById() {
        String jobId = UUID.randomUUID().toString();
        Job job = new Job(jobId, "Job Title", new byte[0], new byte[0], new byte[0], new byte[0], new byte[0], "Job Description", Instant.now().getEpochSecond());

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        Optional<JobDTO> jobDTO = jobService.findJobById(jobId);

        assertTrue(jobDTO.isPresent());
        assertEquals(jobId, jobDTO.get().id());
        verify(jobRepository, times(1)).findById(jobId);
    }

    @Test
    public void testUpdateJob() throws Exception {
        String jobId = UUID.randomUUID().toString();
        Job job = new Job(jobId, "Job Title", "Main image content".getBytes(), "Sub image 1 content".getBytes(), "Sub image 2 content".getBytes(), "Sub image 3 content".getBytes(), "Sub image 4 content".getBytes(), "Job Description", Instant.now().getEpochSecond());

        MockMultipartFile subImage1 = new MockMultipartFile("subImage1", "subImage1.png", "image/png", "Updated Sub image 1 content".getBytes());
        JobEditDTO jobEditDTO = new JobEditDTO("id","Job Title", false, false, subImage1, false, false, null, false, false, null, null, false, null, null, "description");

        when(jobRepository.findById(jobId)).thenReturn(Optional.of(job));

        jobService.updateJob(jobId, jobEditDTO);

        verify(jobRepository, times(1)).findById(jobId);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    public void testDeleteJobById() {
        String jobId = UUID.randomUUID().toString();

        jobService.deleteJobById(jobId);

        verify(jobRepository, times(1)).deleteById(jobId);
    }
}