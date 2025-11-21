package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.managers.MemoryManager;

import java.util.ArrayList;

public class GameSession {
    public GameState state;
    long nextStoneSpawnTime;
    long sessionStartTime;
    long pauseStartTime;
    private int score;
    int climbedStonesNumber;

    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
        sessionStartTime = TimeUtils.millis();
        nextStoneSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN
                * getStonePeriodCoolDown());
        score=0;
        climbedStonesNumber=0;
    }

    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }

    public void resumeGame() {
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
    }
    public void endGame() {
        updateScore();
        state = GameState.ENDED;
        ArrayList<Integer> recordsTable = MemoryManager.loadRecordsTable();
        if (recordsTable == null) {
            recordsTable = new ArrayList<>();
        }
        int foundIdx = 0;
        for (; foundIdx < recordsTable.size(); foundIdx++) {
            if (recordsTable.get(foundIdx) < getScore()) break;
        }
        recordsTable.add(foundIdx, getScore());
        MemoryManager.saveTableOfRecords(recordsTable);
    }
    public void climbingRegistration() {
        climbedStonesNumber += 1;
    }

    public void updateScore() {
        score = (int) (TimeUtils.millis() - sessionStartTime) / 100 + climbedStonesNumber * 100;
    }

    public int getScore() {
        return score;
    }

    public boolean shouldSpawnStone() {
        if (nextStoneSpawnTime <= TimeUtils.millis()) {
            nextStoneSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN
                    * getStonePeriodCoolDown());
            return true;
        }
        return false;
    }

    private float getStonePeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }

}