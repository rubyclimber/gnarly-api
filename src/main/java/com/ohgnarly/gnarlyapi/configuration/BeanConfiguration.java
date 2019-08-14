package com.ohgnarly.gnarlyapi.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.ohgnarly.gnarlyapi.model.Category;
import com.ohgnarly.gnarlyapi.model.Message;
import com.ohgnarly.gnarlyapi.model.User;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static com.ohgnarly.gnarlyapi.constant.GnarlyConstants.*;
import static java.lang.Integer.parseInt;
import static java.lang.System.getenv;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.springframework.util.StringUtils.isEmpty;

@Configuration
@ComponentScan
public class BeanConfiguration {
    @Bean
    public MongoClientSettings mongoClientSettings(ApplicationProperties applicationProperties) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).register(User.class).build()));

        String databaseUser = isEmpty(getenv(DATABASE_USER))
                ? applicationProperties.getDatabaseUser()
                : getenv(DATABASE_USER);

        String databaseName = isEmpty(getenv(DATABASE_NAME))
                ? applicationProperties.getDatabaseName()
                : getenv(DATABASE_NAME);

        String databasePassword = isEmpty(getenv(DATABASE_PASSWORD))
                ? applicationProperties.getDatabasePassword()
                : getenv(DATABASE_PASSWORD);

        int databasePort = isEmpty(getenv(DATABASE_PORT))
                ? applicationProperties.getDatabasePort()
                : parseInt(getenv(DATABASE_PORT));

        String databaseHost = isEmpty(getenv(DATABASE_HOST))
                ? applicationProperties.getDatabaseHost()
                : getenv(DATABASE_HOST);

        MongoCredential mongoCredential = MongoCredential.createCredential(databaseUser, databaseName,
                databasePassword.toCharArray());

        return MongoClientSettings
                .builder()
                .codecRegistry(pojoCodecRegistry)
                .applyToClusterSettings(builder -> {
                    builder.hosts(Collections.singletonList(new ServerAddress(databaseHost, databasePort)));
                })
                .credential(mongoCredential)
                .build();
    }

    @Bean
    public MongoClient mongoClient(MongoClientSettings mongoClientSettings) {
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoDatabase mongoDatabase(MongoClient mongoClient, ApplicationProperties applicationProperties) {
        String databaseName = isEmpty(getenv(DATABASE_NAME))
                ? applicationProperties.getDatabaseName()
                : getenv(DATABASE_NAME);
        return mongoClient.getDatabase(databaseName);
    }

    @Bean
    public MongoCollection<User> userCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection("Users", User.class);
    }

    @Bean
    public MongoCollection<User> chatUserCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection("ChatUsers", User.class);
    }

    @Bean
    public MongoCollection<Message> messageCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection("Messages", Message.class);
    }

    @Bean
    public MongoCollection<Category> categoryCollection(MongoDatabase mongoDatabase) {
        return mongoDatabase.getCollection("Categories", Category.class);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
