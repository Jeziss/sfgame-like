package com.kuk.sfgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.kuk.sfgame.repository.GuildRepository;

@Configuration
public class RepositoryConfig {
    
    private String resolveSqlPropertiesPath() throws Exception {
        return ResourceUtils.getFile("classpath:db/sql.properties").getAbsolutePath();
    }
}
