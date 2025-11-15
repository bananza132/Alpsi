package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.screens.ScreenGame;

public class MyGdxGame extends Game {
	public SpriteBatch batch;
	public OrthographicCamera camera;

	float accumulator=0;
	public ScreenGame screenGame;
	public World world;

	@Override
	public void create() {
		Box2D.init();
		world=new World(new Vector2(0,-10),true);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

		screenGame = new ScreenGame(this);

		setScreen(screenGame);
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

