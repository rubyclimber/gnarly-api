package com.ohgnarly.gnarlyapi.configuration;

import com.mongodb.ConnectionString;
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
import io.github.cdimascio.dotenv.Dotenv;
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
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.springframework.util.StringUtils.isEmpty;

@Configuration
@ComponentScan
public class BeanConfiguration {
    @Bean
    public GnarlyProperties gnarlyProperties(ApplicationProperties applicationProperties) {
        String socketUrl = isNotEmpty(getenv(SOCKET_URL))
                ? getenv(SOCKET_URL)
                : applicationProperties.getSocketUrl();

        GnarlyProperties gnarlyProperties = new GnarlyProperties();
        gnarlyProperties.setSocketUrl(socketUrl);
        return gnarlyProperties;
    }

    @Bean
    public Dotenv dotenv() {
        return Dotenv
                .configure()
                .ignoreIfMissing()
                .ignoreIfMalformed()
                .load();
    }
    @Bean
    public MongoClientSettings mongoClientSettings(ApplicationProperties applicationProperties, Dotenv dotenv) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).register(User.class).build()));

        ConnectionString connectionString = new ConnectionString(
                dotenv.get("MONGO_DB_CONNECTION_STRING", "")
        );

        return MongoClientSettings
                .builder()
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(connectionString)
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
