package com.afrina.jobms.job;

import java.util.List;

import com.afrina.jobms.job.dto.JobWithCompanyDTO;

public interface JobService {
    List<JobWithCompanyDTO> findAll();
    void createJob(Job job);
    Job getJobById(Long id);
    boolean deleteJobById(Long id);
    boolean updateJob(Long id, Job updatedJob);

}
