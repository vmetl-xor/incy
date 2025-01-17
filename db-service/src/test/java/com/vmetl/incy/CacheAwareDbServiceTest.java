package com.vmetl.incy;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.sql.*;
import java.util.List;
import java.util.Map;

@SpringBootTest
class CacheAwareDbServiceTest {

    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass")
                    .withInitScript("initDb.sql");

    private static Connection connection;

    @Autowired
    private CacheAwareDbService cacheAwareDbService;

    @BeforeAll
    static void beforeAll() throws SQLException {
        postgreSQLContainer.start();
        // Obtain JDBC URL, username, and password from the container
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        // Establish connection
        connection = DriverManager.getConnection(jdbcUrl, username, password);
    }

    @AfterAll
    static void afterAll() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }

        postgreSQLContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.port", postgreSQLContainer::getFirstMappedPort);
        registry.add("spring.datasource.host", postgreSQLContainer::getHost);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.database", () -> "testdb");
        registry.add("spring.datasource.driver", () -> "postgresql");
        registry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
    }


    @Test
    @Order(1)
    void testAddSite() throws SQLException {
        String expectedSiteName = "https://vmetl-incy-alpine/";
        cacheAwareDbService.addSite(expectedSiteName);
        try(ResultSet sites = connection.createStatement().
                executeQuery("select * from sites where name = '" + expectedSiteName + "'")) {
            Assertions.assertTrue(sites.next());
            Assertions.assertFalse(sites.next()); //only one must exist
//            assertThat(sites.getString("name")).isEqualTo(expectedSiteName);
        }
    }

    @Test
    @Order(2)
    void testGetAllStream() {
        String name = "www.test.com";
        cacheAwareDbService.addSite(name);
        int siteId = cacheAwareDbService.getSiteIdByName(name).orElseThrow();
        cacheAwareDbService.updateSiteStatistics(siteId, Map.of("one", 1));

        Flux<SiteStats> siteStatsStream = cacheAwareDbService.getSiteStatsStream();
        SiteStats expectedSiteStats1 = new SiteStats(name, (long) siteId, List.of(new WordStats("one", 1)));
        SiteStats expectedSiteStats0 = new SiteStats("https://vmetl-incy-alpine/", 1L, List.of());
        StepVerifier.create(siteStatsStream).
                expectNext(expectedSiteStats0).
                expectNext(expectedSiteStats1).expectComplete().verify();
    }

    @Test
    public void testSchemaExists() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet schemas = metaData.getSchemas()) {
            boolean schemaFound = false;
            while (schemas.next()) {
                String schema = schemas.getString("TABLE_SCHEM");
                if ("public".equals(schema)) {
                    schemaFound = true;
                    break;
                }
            }
            Assertions.assertTrue(schemaFound, "Schema 'test_schema' should exist");
        }
    }

    @Test
    public void testSitesTableExists() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet tables = metaData.getTables(null, "public", "sites", null)) {
            Assertions.assertTrue(tables.next(), "Table 'sites' should exist in 'public'");
        }
    }

    @Test
    public void testWordsTableExists() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet tables = metaData.getTables(null, "public", "words", null)) {
            Assertions.assertTrue(tables.next(), "Table 'words' should exist in 'public'");
        }
    }

    @Test
    public void testSiteStatisticsTableExists() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet tables = metaData.getTables(null, "public", "site_statistics", null)) {
            Assertions.assertTrue(tables.next(), "Table 'site_statistics' should exist in 'public'");
        }
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