package com.niceprogrammer.flappeebee.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.niceprogrammer.flappeebee.FlappeeBeeGame;

public class StartScreen extends ScreenAdapter {

	private static final float WORLD_WIDTH = 480, WORLD_HEIGHT = 640;

	private FlappeeBeeGame game;
	private Stage stage;
	private Texture backgroundTexture, playTexture, playPressTexture, titleTexture;

	public StartScreen(FlappeeBeeGame game) {
		this.game = game;
	}

	public void show() {
		stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
		Gdx.input.setInputProcessor(stage);

		backgroundTexture = new Texture(Gdx.files.internal("bg.png"));
		playTexture = new Texture(Gdx.files.internal("play.png"));
		playPressTexture = new Texture(Gdx.files.internal("playPress.png"));
		titleTexture = new Texture(Gdx.files.internal("title.png"));

		Image background = new Image(backgroundTexture);
		Image title = getTitleImage();
		ImageButton play = getPlayButton();

		stage.addActor(background);
		stage.addActor(title);
		stage.addActor(play);
	}

	private ImageButton getPlayButton() {
		ImageButton play = new ImageButton(new TextureRegionDrawable(new TextureRegion(playTexture)),
				new TextureRegionDrawable(new TextureRegion(playPressTexture)));
		play.setPosition(WORLD_WIDTH / 2, WORLD_HEIGHT / 4, Align.center);
		play.addListener(new ActorGestureListener() {

			@Override
			public void tap(InputEvent event, float x, float y, int count, int button) {
				super.tap(event, x, y, count, button);
				game.setScreen(new LoadingScreen(game));
				dispose();
			}
		});
		return play;
	}

	private Image getTitleImage() {
		Image title = new Image(titleTexture);
		title.setPosition(WORLD_WIDTH / 2, 3 * WORLD_HEIGHT / 4, Align.center);
		return title;
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
	}

	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
		backgroundTexture.dispose();
		playTexture.dispose();
		playPressTexture.dispose();
		titleTexture.dispose();
	}

}