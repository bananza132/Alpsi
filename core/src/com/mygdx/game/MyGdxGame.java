package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.managers.AudioManager;
import com.mygdx.game.screens.ScreenGame;
import com.mygdx.game.screens.ScreenInventory;
import com.mygdx.game.screens.ScreenMenu;
import com.mygdx.game.screens.ScreenSettings;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;

	public BitmapFont largeWhiteFont;
	public BitmapFont commonWhiteFont;
	public BitmapFont commonBlackFont;
	public AudioManager audioManager;

	float accumulator=0;
	public ScreenGame screenGame;
	public ScreenMenu screenMenu;
	public ScreenSettings screenSettings;
	public ScreenInventory screenInventory;

	public World world;

	public Vector3 touch;

	@Override
	public void create() {
		Box2D.init();
		world=new World(new Vector2(0,-10),true);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

		audioManager = new AudioManager();
		largeWhiteFont = FontBuilder.generate(48, Color.WHITE, GameResources.FONT_PATH);
		commonWhiteFont = FontBuilder.generate(24, Color.WHITE, GameResources.FONT_PATH);
		commonBlackFont = FontBuilder.generate(24, Color.BLACK, GameResources.FONT_PATH);

		screenGame = new ScreenGame(this);
		screenInventory = new ScreenInventory(this);
		screenSettings = new ScreenSettings(this);
		screenMenu = new ScreenMenu(this);

		setScreen(screenMenu);
	}

	public void stepWorld(){
		float delta = Gdx.graphics.getDeltaTime();
		accumulator += delta;

		if (accumulator >= GameSettings.STEP_TIME) {
			accumulator -= GameSettings.STEP_TIME;
			world.step(GameSettings.STEP_TIME, GameSettings.VELOCITY_ITERATIONS, GameSettings.POSITION_ITERATIONS);
		}

	}
	@Override
	public void dispose() {
		batch.dispose();
	}

}

