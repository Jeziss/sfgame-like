package com.kuk.sfgame.config;

import java.io.InputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.kuk.sfgame.repository.GuildRepository;

@Configuration
public class RepositoryConfig {
    
    private Resource getSqlPropertiesResource() {
        return new ClassPathResource("db/sql.properties");
    }

    @Bean
    public GuildRepository configureGuildRepository(GuildRepository repo) throws IOException {
        try (InputStream input = getSqlPropertiesResource().getInputStream()) {
            repo.loadSqlQueries(input);
        }
        return repo;
    }


}
