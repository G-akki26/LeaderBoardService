package com.intuit.leaderBoardService.Services;

import java.util.ArrayList;
import java.util.List;

import com.intuit.leaderBoardService.Models.PlayerScore;
import com.intuit.leaderBoardService.Models.Score;
import com.intuit.leaderBoardService.Repository.IPlayerScoreRepository;
import org.springframework.stereotype.Service;

@Service
public class TopScoresService {

    private final IPlayerScoreRepository playerScoreRepository;
    public static final int TOP_N = 5;

    public TopScoresService(IPlayerScoreRepository IPlayerScoreRepository) {
        this.playerScoreRepository = IPlayerScoreRepository;
    }

    public List<Score> getTopScores() {
        List<PlayerScore> playerScores = playerScoreRepository.getTopNByScore(TOP_N);
        List<Score> scores = new ArrayList<>();

        for (PlayerScore playerScore : playerScores) {
            scores.add(new Score(playerScore));
        }

        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        return scores;
    }
}
