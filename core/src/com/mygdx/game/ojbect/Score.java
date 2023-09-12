package com.mygdx.game.ojbect;

public class Score {

    public Score() {
        score = 0;
    }
    private int score;

    public void increaseScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }
}
