package com.example.backend.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Configuration
public class DatabaseInitializer {
    
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
            // Drop tables and sequences if they exist
            jdbc.execute("BEGIN EXECUTE IMMEDIATE 'DROP TABLE utilisateurs CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;");
            jdbc.execute("BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE utilisateurs_seq'; EXCEPTION WHEN OTHERS THEN NULL; END;");
            
            // Create sequences for ID generation
            jdbc.execute("CREATE SEQUENCE utilisateurs_seq START WITH 1 INCREMENT BY 1");
            
            // Create utilisateurs table using sequence
            jdbc.execute("CREATE TABLE utilisateurs (" +
                    "id NUMBER PRIMARY KEY, " +
                    "nom VARCHAR2(255) NOT NULL, " +
                    "prenom VARCHAR2(255) NOT NULL, " +
                    "email VARCHAR2(255) NOT NULL UNIQUE, " +
                    "password VARCHAR2(255) NOT NULL, " +
                    "role VARCHAR2(20), " +
                    "created_at TIMESTAMP, " +
                    "last_connection TIMESTAMP, " +
                    "status NUMBER(1) DEFAULT 0 NOT NULL)");
                    
            // Create trigger for utilisateurs ID generation
            jdbc.execute(
                "CREATE OR REPLACE TRIGGER utilisateurs_bi " +
                "BEFORE INSERT ON utilisateurs " +
                "FOR EACH ROW " +
                "BEGIN " +
                "  SELECT utilisateurs_seq.NEXTVAL " +
                "  INTO :new.id " +
                "  FROM dual; " +
                "END;"
            );
       
            
        } catch (Exception e) {
            // Print the full stack trace for debugging
            e.printStackTrace();
        }
    }
}
