package com.intuit.leaderBoardService.Services;

import com.intuit.leaderBoardService.Models.PlayerScore;
import com.intuit.leaderBoardService.Models.Score;
import com.intuit.leaderBoardService.Repository.IPlayerScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TopScoresServiceTest {

    @Mock
    private IPlayerScoreRepository playerScoreRepository;

    @InjectMocks
    private TopScoresService topScoresService;

    private List<PlayerScore> mockPlayerScores;

    @BeforeEach
    public void setUp() {
        mockPlayerScores = Arrays.asList(
                new PlayerScore("Player1", 300, LocalDateTime.now()),
                new PlayerScore("Player2", 200, LocalDateTime.now()),
                new PlayerScore("Player3", 400, LocalDateTime.now()),
                new PlayerScore("Player4", 100, LocalDateTime.now()),
                new PlayerScore("Player5", 250, LocalDateTime.now())
        );
    }

    @Test
    public void testGetTopScores() {
        when(playerScoreRepository.getTopNByScore(TopScoresService.TOP_N))
                .thenReturn(mockPlayerScores);

        List<Score> topScores = topScoresService.getTopScores();

        assertEquals(5, topScores.size());
        assertEquals(400, topScores.get(0).getScore());
        assertEquals(300, topScores.get(1).getScore());
        assertEquals(250, topScores.get(2).getScore());
        assertEquals(200, topScores.get(3).getScore());
        assertEquals(100, topScores.get(4).getScore());
    }
}
