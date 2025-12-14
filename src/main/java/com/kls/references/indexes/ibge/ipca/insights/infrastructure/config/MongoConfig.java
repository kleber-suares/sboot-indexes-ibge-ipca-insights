package com.kls.references.indexes.ibge.ipca.insights.infrastructure.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Collections;

@Configuration
@Profile("!test")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.password}")
    private String password;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.authentication-database}")
    private String authDatabase;
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private int port;


    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {

        MongoCredential credential = MongoCredential.createCredential(
            username,
            authDatabase,
            password.toCharArray()
        );

        builder
            .credential(credential)
            .applyToClusterSettings(settings ->
                settings.hosts(Collections.singletonList(new ServerAddress(host, port)))
            );
    }

    @Override
    protected String getDatabaseName() {
        return this.database;
    }
}