package com.oxygen.bunker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.oxygen.bunker.BunkerBuster;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = BunkerBuster.WINDOW_HEIGHT;
		config.width = BunkerBuster.WINDOW_WIDTH;
		new LwjglApplication(new BunkerBuster(), config);
	}
}
