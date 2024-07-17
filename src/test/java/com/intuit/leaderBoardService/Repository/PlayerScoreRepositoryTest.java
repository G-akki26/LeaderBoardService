package com.intuit.leaderBoardService.Repository;

import com.intuit.leaderBoardService.Models.PlayerScore;
import com.intuit.leaderBoardService.Services.TopScoresService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerScoreRepositoryTest {

    @Mock
    private MongoCollection<PlayerScore> playerScoreCollection;

    @InjectMocks
    private PlayerScoreRepository playerScoreRepository;

    private List<PlayerScore> mockPlayerScores;

    @BeforeEach
    public void setUp() {
        playerScoreRepository = new PlayerScoreRepository(playerScoreCollection);
        mockPlayerScores = Arrays.asList(
                new PlayerScore("Player1", 100, LocalDateTime.now()),
                new PlayerScore("Player2", 200, LocalDateTime.now()),
                new PlayerScore("Player3", 300, LocalDateTime.now()),
                new PlayerScore("Player4", 400, LocalDateTime.now()),
                new PlayerScore("Player5", 500, LocalDateTime.now())
        );
    }

    @Test
    public void testGetTopNByScore_EmptyQueueAndDb() {
        FindIterable<PlayerScore> findIterable = mockFindIterable(new ArrayList<>());
        when(playerScoreCollection.find()).thenReturn(findIterable);

        List<PlayerScore> result = playerScoreRepository.getTopNByScore(TopScoresService.TOP_N);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTopNByScore_WithData() {
        FindIterable<PlayerScore> findIterable = mockFindIterable(mockPlayerScores);
        when(playerScoreCollection.find()).thenReturn(findIterable);

        List<PlayerScore> result = playerScoreRepository.getTopNByScore(TopScoresService.TOP_N);
        assertEquals(5, result.size());
        assertEquals(500, result.stream().max(Comparator.comparingInt(PlayerScore::getScore)).get().getScore());
    }

    @Test
    public void testAddPlayerScore_EmptyQueueAndDb() {
        FindIterable<PlayerScore> findIterable = mockFindIterable(new ArrayList<>());
        when(playerScoreCollection.find()).thenReturn(findIterable);

        PlayerScore newScore = new PlayerScore("Player6", 600, LocalDateTime.now());
        InsertOneResult insertOneResult = mock(InsertOneResult.class);
        when(playerScoreCollection.insertOne(any(PlayerScore.class))).thenReturn(insertOneResult);
        when(insertOneResult.wasAcknowledged()).thenReturn(true);

        Boolean result = playerScoreRepository.addPlayerScore(newScore);
        assertTrue(result);
        assertEquals(1, playerScoreRepository.getTopNByScore(TopScoresService.TOP_N).size());
    }

    @Test
    public void testAddPlayerScore_FullQueue() {
        FindIterable<PlayerScore> findIterable = mockFindIterable(mockPlayerScores);
        when(playerScoreCollection.find()).thenReturn(findIterable);
        playerScoreRepository.getTopNByScore(TopScoresService.TOP_N);

        PlayerScore newScore = new PlayerScore("Player6", 600, LocalDateTime.now());
        InsertOneResult insertOneResult = mock(InsertOneResult.class);
        when(playerScoreCollection.insertOne(any(PlayerScore.class))).thenReturn(insertOneResult);
        when(insertOneResult.wasAcknowledged()).thenReturn(true);

        Boolean result = playerScoreRepository.addPlayerScore(newScore);
        assertTrue(result);
        List<PlayerScore> topScores = playerScoreRepository.getTopNByScore(TopScoresService.TOP_N);
        assertEquals(5, topScores.size());
        assertEquals(600, topScores.stream().max(Comparator.comparingInt(PlayerScore::getScore)).get().getScore());
    }

    @Test
    public void testAddPlayerScores() {
        FindIterable<PlayerScore> findIterable = mockFindIterable(mockPlayerScores);
        when(playerScoreCollection.find()).thenReturn(findIterable);
        playerScoreRepository.getTopNByScore(TopScoresService.TOP_N);

        List<PlayerScore> newScores = Arrays.asList(
                new PlayerScore("Player6", 600, LocalDateTime.now()),
                new PlayerScore("Player7", 700, LocalDateTime.now())
        );

        InsertManyResult insertManyResult = mock(InsertManyResult.class);
        when(playerScoreCollection.insertMany(anyList())).thenReturn(insertManyResult);
        when(insertManyResult.wasAcknowledged()).thenReturn(true);

        Boolean result = playerScoreRepository.addPlayerScores(newScores);
        assertTrue(result);
        List<PlayerScore> topScores = playerScoreRepository.getTopNByScore(TopScoresService.TOP_N);
        assertEquals(5, topScores.size());
        assertEquals(700, topScores.stream().max(Comparator.comparingInt(PlayerScore::getScore)).get().getScore());
    }

    @SuppressWarnings("unchecked")
    private FindIterable<PlayerScore> mockFindIterable(List<PlayerScore> playerScores) {
        FindIterable<PlayerScore> findIterable = (FindIterable<PlayerScore>) mock(FindIterable.class);
        when(findIterable.sort(any())).thenReturn(findIterable);
        when(findIterable.limit(anyInt())).thenReturn(findIterable);
        when(findIterable.into(any())).thenAnswer(invocation -> {
            List<PlayerScore> list = invocation.getArgument(0);
            list.addAll(playerScores);
            return list;
        });
        return findIterable;
    }
}
