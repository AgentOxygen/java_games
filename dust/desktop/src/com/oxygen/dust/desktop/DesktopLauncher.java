package com.oxygen.dust.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oxygen.dust.DustGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = DustGame.SCREEN_WIDTH;
		config.height = DustGame.SCREEN_HEIGHT;
		new LwjglApplication(new DustGame(), config);
	}
}
