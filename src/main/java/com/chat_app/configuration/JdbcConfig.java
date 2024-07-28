package com.chat_app.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories("com.chat_app.repository")
@EnableJdbcAuditing
public class JdbcConfig {

}
