package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

public class GameSession {
    public GameState state;
    long nextStoneSpawnTime;
    long sessionStartTime;
    long pauseStartTime;

    public GameSession() {
    }

    public void startGame() {
        state = GameState.PLAYING;
        sessionStartTime = TimeUtils.millis();
        nextStoneSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN
                * getStonePeriodCoolDown());
    }

    public void pauseGame() {
        state = GameState.PAUSED;
        pauseStartTime = TimeUtils.millis();
    }

    public void resumeGame() {
        state = GameState.PLAYING;
        sessionStartTime += TimeUtils.millis() - pauseStartTime;
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
