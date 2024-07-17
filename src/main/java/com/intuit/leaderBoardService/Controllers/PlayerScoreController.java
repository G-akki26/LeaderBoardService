package com.intuit.leaderBoardService.Controllers;

import com.intuit.leaderBoardService.Models.Score;
import com.intuit.leaderBoardService.Services.DataIngestionService;
import com.intuit.leaderBoardService.Services.TopScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlayerScoreController {
    private final TopScoresService leaderBoardService;
    private final DataIngestionService dataIngestionService;

    @Autowired
    public PlayerScoreController(
            TopScoresService topScoresService,
            DataIngestionService dataIngestionService) {
        this.leaderBoardService = topScoresService;
        this.dataIngestionService = dataIngestionService;
    }

    @GetMapping("/leaders")
    public List<Score> getPlayerScores() {
        return leaderBoardService.getTopScores();
    }

    @GetMapping("/ingest")
    public void ingestData() {
        dataIngestionService.ingestData();
    }
}
