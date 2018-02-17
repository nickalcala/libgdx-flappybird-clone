package com.niceprogrammer.flappeebee;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.niceprogrammer.flappeebee.screen.StartScreen;

public class FlappeeBeeGame extends Game {

	private final AssetManager assetManager = new AssetManager();

	@Override
	public void create() {
		setScreen(new StartScreen(this));
	}

	public AssetManager getAssetManager() {
		return this.assetManager;
	}
}