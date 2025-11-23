package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.GameDifficult;

import java.util.ArrayList;

public class MemoryManager {
    private static final Preferences preferences = Gdx.app.getPreferences("User saves");

    public static void saveSoundSettings(boolean isOn) {
        preferences.putBoolean("isSoundOn", isOn);
        preferences.flush();
    }

    public static boolean loadIsSoundOn() {
        return preferences.getBoolean("isSoundOn", true);
    }

    public static void saveMusicSettings(boolean isOn) {
        preferences.putBoolean("isMusicOn", isOn);
        preferences.flush();
    }

    public static boolean loadIsMusicOn() {
        return preferences.getBoolean("isMusicOn", true);
    }

    public static void saveTableOfRecords(ArrayList<Integer> table) {

        Json json = new Json();
        String tableInString = json.toJson(table);
        preferences.putString("recordTable", tableInString);
        preferences.flush();
    }

    public static ArrayList<Integer> loadRecordsTable() {
        if (!preferences.contains("recordTable")) return null;

        String scores = preferences.getString("recordTable");
        Json json = new Json();
        ArrayList<Integer> table = json.fromJson(ArrayList.class, scores);
        return table;
    }

    public static void saveDifficult(GameDifficult difficult) {
        if (difficult == GameDifficult.LIGHT) preferences.putInteger("difficult", 1);
        else if (difficult == GameDifficult.MEDIUM) preferences.putInteger("difficult", 2);
        else if (difficult == GameDifficult.HARD) preferences.putInteger("difficult", 3);
        preferences.flush();
    }

    public static GameDifficult loadDifficult() {
        int difficult = preferences.getInteger("difficult", 1);
         if (difficult == 2) return GameDifficult.MEDIUM;
        else if (difficult == 3) return GameDifficult.HARD;
        else return GameDifficult.LIGHT;


    }

}
