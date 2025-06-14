package com.example.backend.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }

    @PostConstruct
    @Transactional
    public void createTablesInOrder() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        try {
            // Check if table exists
            String checkTable = "SELECT COUNT(*) FROM all_tables WHERE owner = 'MYAPP' AND table_name = 'UTILISATEURS'";
            Integer tableCount = jdbc.queryForObject(checkTable, Integer.class);

            if (tableCount == 0) {
                logger.info("Creating MYAPP.UTILISATEURS table and sequence");

                // Create sequence
                jdbc.execute("CREATE SEQUENCE MYAPP.utilisateurs_seq START WITH 1 INCREMENT BY 1");

                // Create table
                jdbc.execute("CREATE TABLE MYAPP.UTILISATEURS (" +
                        "id NUMBER PRIMARY KEY, " +
                        "nom VARCHAR2(255) NOT NULL, " +
                        "prenom VARCHAR2(255) NOT NULL, " +
                        "email VARCHAR2(255) NOT NULL, " +
                        "password VARCHAR2(255) NOT NULL, " +
                        "role VARCHAR2(20), " +
                        "created_at TIMESTAMP, " +
                        "last_connection TIMESTAMP, " +
                        "status NUMBER(1) DEFAULT 0 NOT NULL, " +
                        "CONSTRAINT uk_utilisateurs_email UNIQUE (email))");

                // Create trigger
                jdbc.execute(
                        "CREATE OR REPLACE TRIGGER MYAPP.utilisateurs_bi " +
                                "BEFORE INSERT ON MYAPP.UTILISATEURS " +
                                "FOR EACH ROW " +
                                "BEGIN " +
                                "  SELECT MYAPP.utilisateurs_seq.NEXTVAL " +
                                "  INTO :new.id " +
                                "  FROM dual; " +
                                "END;");

                logger.info("Successfully created MYAPP.UTILISATEURS table and sequence");
            } else {
                logger.info("MYAPP.UTILISATEURS table already exists, skipping creation");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
}