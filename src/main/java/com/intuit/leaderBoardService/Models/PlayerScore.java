package com.intuit.leaderBoardService.Models;

import com.intuit.leaderBoardService.Configurations.MongoConfig;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = MongoConfig.SCORES_COLLECTION_STR)
public class PlayerScore {

    @Id
    private String id;

    @Field("playerName")
    private String playerName;

    @Field("score")
    private int score;

    @Field("dateTime")
    private LocalDateTime dateTime;

    // Constructors, getters, setters
    public PlayerScore() {}

    public PlayerScore(String playerName, int score, LocalDateTime dateTime) {
        this.id = UUID.randomUUID().toString();
        this.playerName = playerName;
        this.score = score;
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
