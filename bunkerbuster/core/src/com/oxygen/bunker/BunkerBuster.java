package com.oxygen.bunker;

import com.badlogic.gdx.Game;

public class BunkerBuster extends Game {
	@Override
	public void create() {
		this.setScreen(new Raid(this));
	}
}
