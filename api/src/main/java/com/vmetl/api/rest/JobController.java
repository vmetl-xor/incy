package com.vmetl.api.rest;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @GetMapping(value = "/{id}")
    public Foo findById(@PathVariable("id") Long id) {
        return new Foo("someId", "someName");
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
