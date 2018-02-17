package com.niceprogrammer.flappeebee.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.niceprogrammer.flappeebee.FlappeeBeeGame;
import com.niceprogrammer.flappeebee.gameobjects.Bee;
import com.niceprogrammer.flappeebee.gameobjects.Flower;

public class GameScreen extends ScreenAdapter {

	private static final float WORLD_WIDTH = 480, WORLD_HEIGHT = 640;
	private static final float GAP_BETWEEN_FLOWERS = 200F;

	// Game
	private FlappeeBeeGame flappeeBeeGame;
	private ShapeRenderer shapeRenderer;
	private Viewport viewport;
	private Camera camera;
	private SpriteBatch batch;
	private int score;
	private BitmapFont bitmapFont;
	private GlyphLayout glyphLayout;

	private TextureRegion background;
	private TextureRegion beeTexture;
	private TextureRegion flowerTop, flowerBottom;

	// Bee
	private Bee bee;

	// Flowers
	private Array<Flower> flowers = new Array<Flower>();

	public GameScreen(FlappeeBeeGame flappeeBeeGame) {
		this.flappeeBeeGame = flappeeBeeGame;
	}

	@Override
	public void show() {
		TextureAtlas atlas = flappeeBeeGame.getAssetManager().get("flappee_bee_assets.atlas");
		background = atlas.findRegion("bg");
		flowerTop = atlas.findRegion("flowerTop");
		flowerBottom = atlas.findRegion("flowerBottom");
		beeTexture = atlas.findRegion("bee");
		bee = new Bee(beeTexture);

		camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
		camera.update();
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		shapeRenderer = new ShapeRenderer();
		batch = new SpriteBatch();
		bee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
		bitmapFont = flappeeBeeGame.getAssetManager().get("score.fnt");
		glyphLayout = new GlyphLayout();
	}

	@Override
	public void render(float delta) {
		clearScreen();
		draw();
		drawDebug();
		update(delta);
	}

	private void draw() {
		batch.totalRenderCalls = 0;
		batch.setProjectionMatrix(camera.projection);
		batch.setTransformMatrix(camera.view);
		batch.begin();
		batch.draw(background, 0, 0);
		drawFlowers();
		bee.draw(batch);
		drawScore();
		batch.end();
		Gdx.app.log("Debug", String.valueOf(batch.totalRenderCalls));
	}

	private void drawFlowers() {
		for (Flower f : flowers) {
			f.draw(batch);
		}
	}

	private void drawScore() {
		String scoreAsString = Integer.toString(score);
		glyphLayout.setText(bitmapFont, scoreAsString);
		bitmapFont.draw(batch, scoreAsString, (viewport.getWorldWidth() - glyphLayout.width) / 2,
				(4 * viewport.getWorldHeight() / 5) - glyphLayout.height / 2);
	}

	private void drawDebug() {
		shapeRenderer.setProjectionMatrix(camera.projection);
		shapeRenderer.setTransformMatrix(camera.view);
		shapeRenderer.begin(ShapeType.Line);
		bee.drawDebug(shapeRenderer);
		for (Flower f : flowers) {
			f.drawDebug(shapeRenderer);
		}
		shapeRenderer.end();
	}

	private void restart() {
		bee.setPosition(WORLD_WIDTH / 4, WORLD_HEIGHT / 2);
		flowers.clear();
		score = 0;
	}

	private void update(float delta) {
		updateBee(delta);
		updateFlowers(delta);
		updateScore();
		if (checkForCollision()) {
			restart();
		}
	}

	private void updateBee(float delta) {
		bee.update(delta);
		if (Gdx.input.isTouched()) {
			bee.flyUp();
		}
		blockFlapeeBeeLeavingTheWorld();
	}

	private void updateFlowers(float delta) {
		for (Flower f : flowers) {
			f.update(delta);
		}
		checkIfNewFlowerIsNeeded();
		removeFlowersIfPassed();
	}

	private void updateScore() {
		for (Flower f : flowers) {
			if (f.getX() < bee.getX() && !f.isPointClaimed()) {
				score++;
				f.markPointClaimed();
			}
		}
	}

	private void createNewFlower() {
		Flower newFlower = new Flower(flowerBottom, flowerTop);
		newFlower.setPosition(WORLD_WIDTH + Flower.WIDTH);
		flowers.add(newFlower);
	}

	private void checkIfNewFlowerIsNeeded() {
		if (flowers.size == 0) {
			createNewFlower();
		} else {
			Flower flower = flowers.peek();
			if (flower.getX() < WORLD_WIDTH - GAP_BETWEEN_FLOWERS) {
				createNewFlower();
			}
		}
	}

	private void removeFlowersIfPassed() {
		if (flowers.size > 0) {
			Flower firstFlower = flowers.first();
			if (firstFlower.getX() < -Flower.WIDTH) {
				flowers.removeValue(firstFlower, true);
			}
		}
	}

	private boolean checkForCollision() {
		for (Flower f : flowers) {
			if (f.isBeeColliding(bee)) {
				return true;
			}
		}
		return false;
	}

	private void blockFlapeeBeeLeavingTheWorld() {
		bee.setPosition(bee.getX(), MathUtils.clamp(bee.getY(), 0, WORLD_HEIGHT));
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