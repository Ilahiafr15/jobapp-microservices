package com.afrina.jobms.job.impl;

//import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.afrina.jobms.job.Job;
import com.afrina.jobms.job.JobRepository;
import com.afrina.jobms.job.JobService;
import com.afrina.jobms.job.dto.JobWithCompanyDTO;
import com.afrina.jobms.job.external.Company;



@Service
public class JobServiceImpl implements JobService{
   // private List<Job> jobs = new ArrayList<>();
   JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
    this.jobRepository = jobRepository;
}


    // private Long nextId = 1L;

    //this below command set gives us proof that we are able to communicate to company microservice from job microservice with the help of RestTemplate
    @Override
    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll(); //list all the jobs with the help of the reporsitory interface
        //List<JobWithCompanyDTO> jobWithCompanyDTOs = new ArrayList<>(); //the Arraylist represent <JobWithCompanyDTO> class

        return jobs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private JobWithCompanyDTO convertToDto(Job job){
        
            JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
            jobWithCompanyDTO.setJob(job); //job set here
            RestTemplate restTemplate = new RestTemplate();
            Company company = restTemplate.getForObject("http://localhost:8081/companies/" + job.getCompanyId(), Company.class); //this companyId to get company object for the job. every job we are getting the companyId and we telling company micro service give us the company with the id 
                //restemplate for API call http protocol
                jobWithCompanyDTO.setCompany(company); //company set here

                return jobWithCompanyDTO;
        
    }


    @Override
    public void createJob(Job job) {
        // job.setId(nextId++); //set the current id to the job and then it will incremented by 1
        jobRepository.save(job);
    }


    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);

    }


    @Override
    public boolean deleteJobById(Long id) {
        // Iterator<Job> iterator = jobs.iterator();
        // while (iterator.hasNext()) {
        //     Job job = iterator.next();
        //     if(job.getId().equals(id)){
        //         iterator.remove();
        //         return true;
        //     }
            
        // }
        // return false;

        try{
            jobRepository.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }


    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        //     for (Job job : jobs) {
    //         if (job.getId().equals(id)) {
    //             job.setTitle(updatedJob.getTitle());
    //             job.setDescription(updatedJob.getDescription());
    //             job.setMinSalary(updatedJob.getMinSalary());
    //             job.setMaxSalary(updatedJob.getMaxSalary());
    //             job.setLocation(updatedJob.getLocation());
    //             return true;   
    //         }
    //     }
    //     return false;

        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()){
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setLocation(updatedJob.getLocation());
            jobRepository.save(job);
            return true;
        }
        return false;
    
    }

}
