package com.intuit.leaderBoardService.Services;

import com.intuit.leaderBoardService.Models.PlayerScore;
import com.intuit.leaderBoardService.Repository.IPlayerScoreRepository;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DataIngestionService {
    private final String scoresPath = "C:\\Tech\\leaderBoardService\\scores\\1.txt";

    private final IPlayerScoreRepository playerScoreRepository;
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter formatter;

    public DataIngestionService(IPlayerScoreRepository playerScoreRepository) {
        this.playerScoreRepository = playerScoreRepository;
        this.objectMapper = new ObjectMapper();
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    }

    public void ingestData() {
        try (BufferedReader br = new BufferedReader(new FileReader(scoresPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                JsonNode jsonNode = objectMapper.readTree(line);
                String playerName = jsonNode.get("playerName").asText();
                int score = jsonNode.get("score").asInt();
                LocalDateTime dateTime = LocalDateTime.parse(jsonNode.get("dateTime").asText(), formatter);
                PlayerScore playerScore = new PlayerScore(playerName, score, dateTime);
                playerScoreRepository.addPlayerScore(playerScore);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
