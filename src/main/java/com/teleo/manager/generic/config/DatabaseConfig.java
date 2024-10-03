package com.teleo.manager.generic.config;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.zaxxer.hikari.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackages = {
        "com.teleo.manager.assurance.repositories",
        "com.teleo.manager.authentification.repositories",
        "com.teleo.manager.document.repositories",
        "com.teleo.manager.notification.repositories",
        "com.teleo.manager.paiement.repositories",
        "com.teleo.manager.parametre.repositories",
        "com.teleo.manager.prestation.repositories",
        "com.teleo.manager.rapport.repositories",
        "com.teleo.manager.sinistre.repositories"
})
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        return new HikariDataSource(config);
    }
}
