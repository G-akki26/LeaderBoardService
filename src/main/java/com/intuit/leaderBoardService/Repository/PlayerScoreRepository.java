package com.intuit.leaderBoardService.Repository;

import com.intuit.leaderBoardService.Models.PlayerScore;
import com.intuit.leaderBoardService.Services.TopScoresService;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PlayerScoreRepository implements IPlayerScoreRepository {

    private final MongoCollection<PlayerScore> playerScoreCollection;
    PriorityQueue<PlayerScore> playerScoreQueue;

    @Autowired
    public PlayerScoreRepository(MongoCollection<PlayerScore> playerScoreCollection) {
        this.playerScoreCollection = playerScoreCollection;
        this.playerScoreQueue = new PriorityQueue<PlayerScore>(Comparator.comparing(PlayerScore::getScore));
    }

    @Override
    public List<PlayerScore> getTopNByScore(int n) {
        refreshQueueFromDbIfEmpty();

        return new ArrayList<>(playerScoreQueue);
    }

    @Override
    public Boolean addPlayerScore(PlayerScore playerScore) {
        refreshQueueFromDbIfEmpty();

        if (playerScoreQueue.isEmpty()) {

            playerScoreQueue.add(playerScore);

            return playerScoreCollection.insertOne(playerScore).wasAcknowledged();
        }

        if (playerScoreQueue.size() < TopScoresService.TOP_N) {
            playerScoreQueue.add(playerScore);

            return playerScoreCollection.insertOne(playerScore).wasAcknowledged();
        }

        PlayerScore minScore = playerScoreQueue.peek();

        if (playerScore.getScore() >= minScore.getScore()) {
            playerScoreQueue.add(playerScore);
            playerScoreQueue.remove();
        }

        return playerScoreCollection.insertOne(playerScore).wasAcknowledged();
    }

    @Override
    public Boolean addPlayerScores(List<PlayerScore> scores) {
        refreshQueueFromDbIfEmpty();

        var results = playerScoreCollection.insertMany(scores);

        for (PlayerScore score : scores) {
            if (playerScoreQueue.size() < TopScoresService.TOP_N) {
                playerScoreQueue.add(score);
                continue;
            }

            if (playerScoreQueue.peek().getScore() > score.getScore()) {
                continue;
            }

            playerScoreQueue.remove();
            playerScoreQueue.add(score);
        }

        return results.wasAcknowledged();
    }

    private synchronized void refreshQueueFromDbIfEmpty() {
        if (!playerScoreQueue.isEmpty()) {
            return;
        }

        List<PlayerScore> scores = playerScoreCollection.find()
                .sort(Sorts.descending("score"))
                .limit(TopScoresService.TOP_N)
                .into(new ArrayList<PlayerScore>());

        playerScoreQueue.addAll(scores);
    }
}