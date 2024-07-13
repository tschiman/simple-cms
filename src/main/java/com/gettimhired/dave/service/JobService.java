package com.gettimhired.dave.service;

import com.gettimhired.dave.model.dto.JobDTO;
import com.gettimhired.dave.model.dto.JobFormDTO;
import com.gettimhired.dave.model.mongo.Job;
import com.gettimhired.dave.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void save(JobFormDTO jobFormDto) {
        try {
            var job = new Job(
                    UUID.randomUUID().toString(),
                    jobFormDto.title(),
                    UUID.randomUUID().toString(),
                    jobFormDto.mainImage().getBytes(),
                    jobFormDto.description()
            );
            jobRepository.save(job);
        } catch (Exception e) {
            //TODO
        }
    }

    public List<JobDTO> findAllJobs() {
        return jobRepository.findAll().stream()
                .map(JobDTO::new)
                .toList();
    }

    public byte[] findJobMainImage(String id, String imageId) {
        var job = jobRepository.findByIdAndMainImageId(id, imageId);
        if (job.isPresent()) {
            return job.get().mainImage();
        } else {
            return new byte[0];
        }
    }
}
