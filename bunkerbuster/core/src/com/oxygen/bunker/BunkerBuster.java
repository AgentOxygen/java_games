package com.oxygen.bunker;

import com.badlogic.gdx.Game;

public class BunkerBuster extends Game {
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 800;

	@Override
	public void create() {
		this.setScreen(new Raid());
	}

	public static void print(Object out){
		System.out.println(out.toString());
	}
}
