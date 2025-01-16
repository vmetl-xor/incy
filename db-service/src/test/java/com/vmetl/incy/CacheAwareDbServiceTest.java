package com.vmetl.incy;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.*;

class CacheAwareDbServiceTest {

    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
//            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass")
            .withInitScript("initDb.sql");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @Test
    void addSite() {



    }

    @Test
    void getSiteIdByName() {
    }

    @Test
    void updateSiteStatistics() {
    }

    @Test
    void getSiteStatsStream() {
    }

    @Test
    void getSiteStatsByNameStream() {
    }

    @Test
    void getSiteStats() {
    }
}