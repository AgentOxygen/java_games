package com.agentoxygen.speed;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class SpeedRunner extends Game {

	public static Skin skin;

	@Override
	public void create () {
		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		this.setScreen(new RunScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		skin.dispose();
	}

	public static void print(Object out){
		System.out.println(out.toString());
	}
}
