package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.managers.MemoryManager;

import java.util.ArrayList;

public class GameSession {
    public GameState state;
    public GameDifficult difficult;
    private long nextStoneSpawnTime;
    private long sessionStartTime;
    private long pauseStartTime;
    private int climbedStonesNumber;
    private int score;

    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
        difficult = MemoryManager.loadDifficult();
        sessionStartTime = TimeUtils.millis();
        if (difficult == GameDifficult.MEDIUM)
            nextStoneSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN * getStonePeriodCoolDown());
        else if (difficult == GameDifficult.HARD)
            nextStoneSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN / 10f * getStonePeriodCoolDown());
        else
            nextStoneSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN / 0.75 * getStonePeriodCoolDown());
        score = 0;
        climbedStonesNumber = 0;
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
            if (difficult == GameDifficult.MEDIUM)
                nextStoneSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN * getStonePeriodCoolDown());
            else if (difficult == GameDifficult.HARD)
                nextStoneSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN / 1.75f * getStonePeriodCoolDown());
            else
                nextStoneSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN / 0.75 * getStonePeriodCoolDown());
            return true;
        }
        return false;
    }

    private float getStonePeriodCoolDown() {
        if ((float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000) < 0.5)
            return 0.5f;
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }

    public void changeDifficult(GameDifficult difficult) {
        this.difficult = difficult;
    }
}