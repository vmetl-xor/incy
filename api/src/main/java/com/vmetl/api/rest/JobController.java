package com.vmetl.api.rest;


import com.vmetl.api.service.JobService;
import com.vmetl.incy.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }


    @GetMapping(value = "/{id}")
    public Message<String, String> findById(@PathVariable("id") Long id) {
        return jobService.sendMessage(id + "");

        //        return new Foo("someId", "someName");
//        return RestPreconditions.checkFound(service.findById(id));
    }

    @GetMapping
    public List<Foo> findById() {
        return List.of(new Foo("someId", "someName"), new Foo("someId2", "someName2"));
//        return RestPreconditions.checkFound(service.findById(id));
    }

    public static class Foo {
        private String id;
        private String name;

        public Foo(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
