package com.intuit.leaderBoardService.Models;

public class Score {
    private int score;
    private String playerName;

    public Score(int score, String playerName) {
        this.score = score;
        this.playerName = playerName;
    }

    public Score(PlayerScore score) {
        this.score = score.getScore();
        this.playerName = score.getPlayerName();
    }

    public int getScore() {
        return score;
    }

    public String getPlayerName() {
        return playerName;
    }
}
