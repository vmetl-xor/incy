package com.vmetl.api.rest;


import com.vmetl.api.rest.dto.Job;
import com.vmetl.api.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        Optional<Job> createdJob = jobService.createJob(job);

        return createdJob.
                map(ResponseEntity::ok).
                orElse(ResponseEntity.status(HttpStatusCode.valueOf(429)).build());
    }


    @PostMapping(value = "/stop")
    public ResponseEntity<String> stopAll() {
        jobService.stopAllProcessors();

        return ResponseEntity.ok("All jobs stopped");
    }
}
