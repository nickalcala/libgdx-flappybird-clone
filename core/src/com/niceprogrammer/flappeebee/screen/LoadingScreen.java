package com.niceprogrammer.flappeebee.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.niceprogrammer.flappeebee.FlappeeBeeGame;

public class LoadingScreen extends ScreenAdapter {

	private static final float WORLD_WIDTH = 480, WORLD_HEIGHT = 640;
	private static final float PROGRESS_BAR_WIDTH = 100;
	private static final float PROGRESS_BAR_HEIGHT = 25;

	private ShapeRenderer shapeRenderer;
	private Viewport viewport;
	private Camera camera;
	private float progress = 0;
	private final FlappeeBeeGame flappeeBeeGame;

	public LoadingScreen(FlappeeBeeGame flappeeBeeGame) {
		this.flappeeBeeGame = flappeeBeeGame;
	}

	@Override
	public void show() {
		camera = new OrthographicCamera();
		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
		camera.update();

		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		shapeRenderer = new ShapeRenderer();

		flappeeBeeGame.getAssetManager().load("flappee_bee_assets.atlas", TextureAtlas.class);
		BitmapFontParameter bitmapFontParameter = new BitmapFontParameter();
		bitmapFontParameter.atlasName = "flappee_bee_assets.atlas";
		flappeeBeeGame.getAssetManager().load("score.fnt", BitmapFont.class, bitmapFontParameter);
	}

	@Override
	public void render(float delta) {
		update();
		clearScreen();
		draw();
	}

	private void update() {
		if (flappeeBeeGame.getAssetManager().update()) {
			flappeeBeeGame.setScreen(new GameScreen(flappeeBeeGame));
		} else {
			progress = flappeeBeeGame.getAssetManager().getProgress();
		}
	}

	private void draw() {
		shapeRenderer.setProjectionMatrix(camera.projection);
		shapeRenderer.setTransformMatrix(camera.view);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect((WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2, (WORLD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2,
				PROGRESS_BAR_WIDTH * progress, PROGRESS_BAR_HEIGHT);
		shapeRenderer.end();
	}

	private void clearScreen() {
		Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}