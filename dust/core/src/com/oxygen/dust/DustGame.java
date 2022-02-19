package com.oxygen.dust;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class DustGame extends Game {
	public static final int SCREEN_WIDTH = 1000;
	public static final int SCREEN_HEIGHT = 800;

	@Override
	public void create() {
		InFlightScreen inFlightScreen = new InFlightScreen();
		this.setScreen(inFlightScreen);
	}

	public static void print(Object out){
		System.out.println(out.toString());
	}
}
