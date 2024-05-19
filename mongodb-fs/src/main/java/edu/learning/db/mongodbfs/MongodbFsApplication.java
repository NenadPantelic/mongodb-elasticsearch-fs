package edu.learning.db.mongodbfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MongodbFsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MongodbFsApplication.class, args);
    }

}
