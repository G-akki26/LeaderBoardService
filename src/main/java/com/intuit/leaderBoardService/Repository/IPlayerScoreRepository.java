package com.intuit.leaderBoardService.Repository;

import com.intuit.leaderBoardService.Models.PlayerScore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPlayerScoreRepository {
    List<PlayerScore> getTopNByScore(int n);
    Boolean addPlayerScore(PlayerScore playerScore);
    Boolean addPlayerScores(List<PlayerScore> scores);
}