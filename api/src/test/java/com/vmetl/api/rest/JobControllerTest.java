package com.vmetl.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmetl.api.rest.dto.Job;
import com.vmetl.api.service.JobService;
import com.vmetl.incy.dao.reactive.SiteReactiveDao;
import com.vmetl.incy.metrics.db.DbMetrics;
import com.vmetl.incy.dao.SiteDao;
import com.vmetl.incy.cache.VisitedRefsCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
class JobControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private JobService jobService;

    @MockBean
    private VisitedRefsCache cache;

    @MockBean
    private DbMetrics dbMetrics;

    @MockBean
    private SiteDao siteDao;

    @MockBean
    private SiteReactiveDao siteReactiveDao;

    @Test
    void createJob_withCorrectMessage_shouldCreateNewJob() throws Exception {
        String content = """
                {
                "url": "www.example.com",
                "depth": 0
                }
                """;

        when(jobService.createJob(any(Job.class))).
                thenReturn((Optional.of(new Job("www.example.com", 0))));

        String responseString = mvc.perform(MockMvcRequestBuilders.post("/jobs/add").
                        contentType(MediaType.APPLICATION_JSON).
                        content(content)).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.url").value("www.example.com")).
                andExpect(jsonPath("$.depth").value(0)).
                andReturn().
                getResponse().
                getContentAsString();

        Job message = new ObjectMapper().readValue(responseString, Job.class);
        assertThat(message.getUrl()).isEqualTo("www.example.com");
        assertThat(message.getDepth()).isEqualTo(0);

        verify(jobService, Mockito.times(1)).createJob(Mockito.any());
    }

    @Test
    void createJob_whenDuplicated_shouldReturnError() throws Exception {
        String content = """
                {
                "url": "www.example.com",
                "depth": 0
                }
                """;

        when(jobService.createJob(any(Job.class))).
                thenReturn((Optional.empty()));

        String responseString = mvc.perform(MockMvcRequestBuilders.post("/jobs/add").
                        contentType(MediaType.APPLICATION_JSON).
                        content(content)).
                andExpect(status().is(202)).
                andReturn().
                getResponse().
                getContentAsString();

        verify(jobService, Mockito.times(1)).createJob(Mockito.any());
        assertThat(responseString).isEmpty();
    }

    @Test
    void stopJob_shouldStopJob() throws Exception {
        String responseText = mvc.perform(MockMvcRequestBuilders.post("/jobs/stop")).
                andExpect(status().isOk()).
                andExpect(content().string("All jobs stopped")).andReturn().getResponse().getContentAsString();

        verify(jobService, Mockito.times(1)).stopAllProcessors();
        assertThat(responseText).isEqualTo("All jobs stopped");
    }
}