package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

public class GameSession {
    long nextTrashSpawnTime;
    long sessionStartTime;

    public GameSession() {
    }

    public void startGame() {
        sessionStartTime = TimeUtils.millis();
        nextTrashSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN
                * getStonePeriodCoolDown());
    }

    public boolean shouldSpawnStone() {
        if (nextTrashSpawnTime <= TimeUtils.millis()) {
            nextTrashSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_STONE_APPEARANCE_COOL_DOWN
                    * getStonePeriodCoolDown());
            return true;
        }
        return false;
    }

    private float getStonePeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }

}
