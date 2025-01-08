package com.vmetl.api.rest;


import com.vmetl.api.rest.dto.Job;
import com.vmetl.api.service.JobService;
import com.vmetl.api.service.SitesService;
import com.vmetl.incy.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping(value = "/add")
    public Message createJob(@RequestBody Job job) {
        return jobService.sendMessage(job);
    }


    @GetMapping(value = "/stop")
    public String stopAll() {
        jobService.stopAllProcessors();

        return "All stopped";
    }
}
