package com.gettimhired.simplecms.service;

import com.gettimhired.simplecms.model.dto.JobDTO;
import com.gettimhired.simplecms.model.dto.JobEditDTO;
import com.gettimhired.simplecms.model.dto.JobFormDTO;
import com.gettimhired.simplecms.model.mongo.Job;
import com.gettimhired.simplecms.repository.JobRepository;
import com.gettimhired.simplecms.util.GzipUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
            try {
                return switch (subImageId) {
                    case "1" -> GzipUtil.decompress(jobOpt.get().subImage1());
                    case "2" -> GzipUtil.decompress(jobOpt.get().subImage2());
                    case "3" -> GzipUtil.decompress(jobOpt.get().subImage3());
                    case "4" -> GzipUtil.decompress(jobOpt.get().subImage4());
                    default -> new byte[0];
                };
            } catch (Exception e) {
                return new byte[0];
            }
        } else {
            return new byte[0];
        }
    }

    public Optional<JobDTO> findJobById(String id) {
        return jobRepository.findById(id).map(JobDTO::new);
    }

    public void updateJob(String id, JobEditDTO jobEditDto) {
        var jobFromDB = jobRepository.findById(id);
        if (jobFromDB.isPresent()) {
            //update job
            try {
                var subImage1 = jobEditDto.deleteSubImage1() != null && jobEditDto.deleteSubImage1() ?
                        new byte[0] :
                        jobEditDto.subImage1() != null && jobEditDto.subImage1().getBytes().length > 0 ?
                                GzipUtil.compress(jobEditDto.subImage1().getBytes()) :
                                jobFromDB.get().subImage1();

                var subImage2 = jobEditDto.deleteSubImage2() != null && jobEditDto.deleteSubImage2() ?
                        new byte[0] :
                        jobEditDto.subImage2() != null && jobEditDto.subImage2().getBytes().length > 0 ?
                                GzipUtil.compress(jobEditDto.subImage2().getBytes()) :
                                jobFromDB.get().subImage2();

                var subImage3 = jobEditDto.deleteSubImage3() != null && jobEditDto.deleteSubImage3() ?
                        new byte[0] :
                        jobEditDto.subImage3() != null && jobEditDto.subImage3().getBytes().length > 0 ?
                                GzipUtil.compress(jobEditDto.subImage3().getBytes()) :
                                jobFromDB.get().subImage3();

                var subImage4 = jobEditDto.deleteSubImage4() != null && jobEditDto.deleteSubImage4() ?
                        new byte[0] :
                        jobEditDto.subImage4() != null && jobEditDto.subImage4().getBytes().length > 0 ?
                                GzipUtil.compress(jobEditDto.subImage4().getBytes()) :
                                jobFromDB.get().subImage4();
                

                var jobToSave = new Job(
                        id,
                        jobEditDto.title(),
                        jobFromDB.get().mainImage(),
                        subImage1,
                        subImage2,
                        subImage3,
                        subImage4,
                        jobEditDto.description()
                );
                jobRepository.save(jobToSave);
            } catch (Exception e) {
                //TODO
            }
        }
    }
}
