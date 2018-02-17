package com.niceprogrammer.flappeebee.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.niceprogrammer.flappeebee.FlappeeBeeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 240;
		config.height = 320;
		TexturePacker.process("../assets", "../assets", "flappee_bee_assets");
		new LwjglApplication(new FlappeeBeeGame(), config);
	}
}
