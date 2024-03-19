package org.wsd.app.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "org.wsd.app.mongo")
public class MongoDBConfig {
}
