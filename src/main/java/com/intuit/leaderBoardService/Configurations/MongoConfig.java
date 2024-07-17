package com.intuit.leaderBoardService.Configurations;

import com.intuit.leaderBoardService.Models.PlayerScore;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    public static final String SCORES_COLLECTION_STR = "ScoresCollection";

    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    @Bean
    public MongoClient getMongoClient() {
//        CodecRegistry defaultCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
//        CodecRegistry customCodecRegistry = CodecRegistries.fromCodecs(new LocalDateTimeCodec());
//        CodecRegistry combinedCodecRegistry = CodecRegistries.fromRegistries(defaultCodecRegistry, customCodecRegistry);
//
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .codecRegistry(combinedCodecRegistry)
//                .build();

        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoDatabase getLeaderBoardDb() {
        MongoClient mongoClient = getMongoClient();

        return mongoClient.getDatabase(databaseName);
    }

    @Bean
    public MongoCollection<PlayerScore> getScoresCollection() {
        MongoDatabase database = getLeaderBoardDb();

        MongoCollection<PlayerScore> collection = database.getCollection(SCORES_COLLECTION_STR, PlayerScore.class);

        collection = collection.withCodecRegistry(pojoCodecRegistry);

        return collection;
    }
}
