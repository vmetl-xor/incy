package com.vmetl.api.rest;

import com.vmetl.api.service.JobService;
import com.vmetl.incy.SiteDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class JobControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    SiteDao siteDao;

    @Autowired
    private JobService jobService;

    @Test
    void createJob() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/jobs/add").content("www.example.com")).andExpect(status().isOk());

    }
}