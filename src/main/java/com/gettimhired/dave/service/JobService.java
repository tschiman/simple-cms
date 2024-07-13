package com.gettimhired.dave.service;

import com.gettimhired.dave.model.dto.JobDTO;
import com.gettimhired.dave.model.dto.JobFormDTO;
import com.gettimhired.dave.model.mongo.Job;
import com.gettimhired.dave.repository.JobRepository;
import com.gettimhired.dave.util.GzipUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void save(JobFormDTO jobFormDto) {
        //gzip the image
        try {
            var mainImgGzip = GzipUtil.compress(jobFormDto.mainImage().getBytes());
            var subImage1 = GzipUtil.compress(jobFormDto.subImage1().getBytes());
            var subImage2 = GzipUtil.compress(jobFormDto.subImage2().getBytes());
            var subImage3 = GzipUtil.compress(jobFormDto.subImage3().getBytes());
            var subImage4 = GzipUtil.compress(jobFormDto.subImage4().getBytes());
            var job = new Job(
                    UUID.randomUUID().toString(),
                    jobFormDto.title(),
                    mainImgGzip,
                    subImage1,
                    subImage2,
                    subImage3,
                    subImage4,
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

    public byte[] findJobMainImage(String id) {
        var jobOpt = jobRepository.findById(id);
        if (jobOpt.isPresent()) {
            try {
                return GzipUtil.decompress(jobOpt.get().mainImage());
            } catch (IOException e) {
                return new byte[0];
            }
        } else {
            return new byte[0];
        }
    }

    public byte[] findJobSubImage(String id, String subImageId) {
        var jobOpt = jobRepository.findById(id);
        if (jobOpt.isPresent()) {
            return switch (subImageId) {
                case "1" -> jobOpt.get().subImage1();
                case "2" -> jobOpt.get().subImage2();
                case "3" -> jobOpt.get().subImage3();
                case "4" -> jobOpt.get().subImage4();
                default -> new byte[0];
            };
        } else {
            return new byte[0];
        }
    }
}
