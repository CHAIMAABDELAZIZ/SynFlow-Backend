package com.example.backend.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class HibernateConfig {

    private final Environment env;

    public HibernateConfig(Environment env) {
        this.env = env;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            // Completely disable check constraints for enums
            hibernateProperties.put("hibernate.validator.apply_to_ddl", "false");
            hibernateProperties.put("hibernate.use_nationalized_character_data", "true");
            
            // Important: This avoids the generation of check constraints for enum fields
            hibernateProperties.put("hibernate.type.preferred_enum_type", "string");
            
            // The naming strategy helps with Oracle-compatible column names
            hibernateProperties.put("hibernate.physical_naming_strategy", 
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
        };
    }
}
